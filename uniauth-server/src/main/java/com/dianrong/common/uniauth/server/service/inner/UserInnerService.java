package com.dianrong.common.uniauth.server.service.inner;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.UserIdentityType;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.util.AuthUtils;
import com.dianrong.common.uniauth.common.util.Base64;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.common.util.UniPasswordEncoder;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserPwdLog;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserPwdLogMapper;
import com.dianrong.common.uniauth.server.data.query.UserPwdLogQueryParam;
import com.dianrong.common.uniauth.server.datafilter.DataFilter;
import com.dianrong.common.uniauth.server.datafilter.FieldType;
import com.dianrong.common.uniauth.server.datafilter.FilterType;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.mq.UniauthSender;
import com.dianrong.common.uniauth.server.mq.v1.NotifyInfoType;
import com.dianrong.common.uniauth.server.mq.v1.UniauthNotify;
import com.dianrong.common.uniauth.server.mq.v1.ninfo.BaseUserNotifyInfo;
import com.dianrong.common.uniauth.server.service.common.NotificationService;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;
import com.google.common.collect.Maps;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 *用户Service的内部实现. 
 */
@Slf4j
@Service
public class UserInnerService extends TenancyBasedService {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserPwdLogMapper userPwdLogMapper;
  
  // Notification
  @Autowired
  private NotificationService notificationService;
  @Autowired
  private UniauthSender uniauthSender;
  @Autowired
  private UniauthNotify uniauthNotify;

  // Data filter
  @Resource(name = "userDataFilter")
  private DataFilter dataFilter;

  // 异步操作线程池.
  private static final ExecutorService executor = Executors.newFixedThreadPool(1);

  /**
   * 新增用户.
   */
  @Transactional
  public UserDto addNewUser(String name, String phone, String email) {
    this.checkPhoneAndEmail(phone, email, null);
    User user = new User();
    user.setEmail(email);
    user.setName(name);
    user.setTenancyId(tenancyService.getTenancyIdWithCheck());
    Date now = new Date();
    user.setFailCount(AppConstants.ZERO_BYTE);

    String randomPassword = AuthUtils.randomPassword();
    byte[] salt = AuthUtils.createSalt();
    user.setPassword(Base64.encode(AuthUtils.digest(randomPassword, salt)));
    user.setPasswordSalt(Base64.encode(salt));

    user.setLastUpdate(now);
    user.setCreateDate(now);
    user.setPhone(phone);
    user.setStatus(AppConstants.STATUS_ENABLED);
    userMapper.insert(user);
    UserDto userDto = BeanConverter.convert(user);

    // 用户添加成功后发送mq
    uniauthSender.sendUserAdd(userDto);
    userDto.setPassword(randomPassword);
    asynAddUserPwdLog(user);

    // 发送通知给用户
    notificationService.addUserNotification(user);
    return userDto;
  }

  /**
   * 更新用户信息.
   */
  @Transactional
  public UserDto updateUser(UserActionEnum userActionEnum, Long id, String account, Long tenancyId,
      String name, String phone, String email, String password, String orginPassword,
      Boolean ignorePwdStrategyCheck, Byte status) {
    if (userActionEnum == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "userActionEnum"));
    }
    User user;
    String userIdentity;
    // 通过账号的查找当前的用户信息
    if (UserActionEnum.isUpdateByAccount(userActionEnum)) {
      if (account == null || tenancyId == null) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.parameter.empty", "account, tenancyId"));
      }
      user = getUserByAccount(account, null, tenancyId, true, AppConstants.STATUS_ENABLED);
      userIdentity = account;
    } else {
      if (id == null) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.parameter.empty", "userId"));
      }
      user = getUserByPrimaryKey(id);
      userIdentity = String.valueOf(id);
    }
    if (user == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.notfound", userIdentity, User.class.getSimpleName()));
    } else if (AppConstants.ONE_BYTE.equals(user.getStatus())
        && !UserActionEnum.STATUS_CHANGE.equals(userActionEnum)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.entity.status.isone", userIdentity, User.class.getSimpleName()));
    }
    // 辅助标识位
    boolean isUpdatePassword = false;
    switch (userActionEnum) {
      case LOCK:
        user.setFailCount(AppConstants.MAX_AUTH_FAIL_COUNT);
        break;
      case UNLOCK:
        user.setFailCount(AppConstants.ZERO_BYTE);
        break;
      case RESET_PASSWORD:
        isUpdatePassword = true;
        checkUserPwd(user.getId(), password, ignorePwdStrategyCheck);
        byte[] salt = AuthUtils.createSalt();
        user.setPassword(Base64.encode(AuthUtils.digest(password, salt)));
        user.setPasswordSalt(Base64.encode(salt));
        user.setPasswordDate(new Date());
        // reset failed count
        user.setFailCount(AppConstants.ZERO_BYTE);
        break;
      case STATUS_CHANGE:
        // 只处理启用的情况
        if (status != null && status == AppConstants.STATUS_ENABLED) {
          this.checkPhoneAndEmail(user.getPhone(), user.getEmail(), user.getId());
        }
        user.setStatus(status);
        break;
      case UPDATE_INFO:
        this.checkPhoneAndEmail(phone, email, user.getId());
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        break;
      case UPDATE_INFO_BY_ACCOUNT:
        // 目前只能修改用户的名称
        user.setName(name);
        break;
      case UPDATE_EMAIL_BY_ACCOUNT:
        this.checkEmail(email, user.getId());
        user.setEmail(email);
        break;
      case UPDATE_PHONE_BY_ACCOUNT:
        this.checkPhone(phone, user.getId());
        user.setPhone(phone);
        break;
      case UPDATE_PASSWORD_BY_ACCOUNT:
        // same as case:RESET_PASSWORD_AND_CHECK
      case RESET_PASSWORD_AND_CHECK:
        // 原始密码验证通过
        if (orginPassword == null) {
          throw new AppException(InfoName.VALIDATE_FAIL,
              UniBundle.getMsg("common.parameter.origin.password.wrong"));
        }
        if (!UniPasswordEncoder.isPasswordValid(user.getPassword(), orginPassword,
            user.getPasswordSalt())) {
          throw new AppException(InfoName.VALIDATE_FAIL,
              UniBundle.getMsg("common.parameter.wrong", "origin password"));
        }

        isUpdatePassword = true;
        // 验证新密码
        checkUserPwd(user.getId(), password, ignorePwdStrategyCheck);
        byte[] salttemp = AuthUtils.createSalt();
        user.setPassword(Base64.encode(AuthUtils.digest(password, salttemp)));
        user.setPasswordSalt(Base64.encode(salttemp));
        user.setPasswordDate(new Date());
        user.setFailCount(AppConstants.ZERO_BYTE);
        break;
      default:
        break;
    }
    user.setLastUpdate(new Date());
    if (isUpdatePassword) {
      // 特殊设置的密码, 需要在登陆的时候重新设置密码
      if (ignorePwdStrategyCheck != null && ignorePwdStrategyCheck) {
        user.setPasswordDate(null);
      }
    }

    userMapper.updateByPrimaryKey(user);

    // 记录日志设置记录
    if (isUpdatePassword) {
      // 如果特设设置的密码, 则不记录密码设置日志
      if (ignorePwdStrategyCheck == null || !ignorePwdStrategyCheck) {
        asynAddUserPwdLog(user);
      }
    }

    // 发送通知
    if (UserActionEnum.STATUS_CHANGE.equals(userActionEnum)) {
      BaseUserNotifyInfo notifyInfo = new BaseUserNotifyInfo();
      notifyInfo.setUserId(user.getId());
      // 只处理启用的情况
      if (status != null && status == AppConstants.STATUS_ENABLED) {
        notifyInfo.setType(NotifyInfoType.USER_ENABLE);
      } else {
        notifyInfo.setType(NotifyInfoType.USER_DISABLE);
      }
      uniauthNotify.notify(notifyInfo);
    }

    // 通知用户密码修改了
    if (UserActionEnum.isPasswordChange(userActionEnum)) {
      notificationService.updateUserPwdNotification(user);
    }

    return BeanConverter.convert(user).setPassword(password);
  }
  
  /**
   * 更新用户密码.
   */
  @Transactional
  public void resetPassword(UserParam userParam) {
    String email = userParam.getEmail();
    String password = userParam.getPassword();
    CheckEmpty.checkAllBlank("邮件手机", email, userParam.getPhone());
    CheckEmpty.checkEmpty(password, "密码");
    User user = null;
    if (email != null) {
      user = getUserByAccount(email, userParam.getTenancyCode(), userParam.getTenancyId(), false,
          AppConstants.STATUS_ENABLED);
    } else {
      user = getUserByAccount(userParam.getPhone(), userParam.getTenancyCode(),
          userParam.getTenancyId(), true, AppConstants.STATUS_ENABLED);
    }

    checkUserPwd(user.getId(), password, false);
    byte[] salt = AuthUtils.createSalt();
    user.setPassword(Base64.encode(AuthUtils.digest(password, salt)));
    user.setPasswordSalt(Base64.encode(salt));
    user.setPasswordDate(new Date());
    user.setFailCount(AppConstants.ZERO_BYTE);
    userMapper.updateByPrimaryKey(user);

    // add password set log
    asynAddUserPwdLog(user);
  }

  /**
   * 根据用户的Identity和Identity的类型获取用户信息.
   * 
   * @param identity 用户的标识信息,不能为空.
   * @param identityType 标识类型,不能为空.
   */
  public User getUserByIdentity(String identity, UserIdentityType identityType) {
    CheckEmpty.checkEmpty(identity, "identity");
    CheckEmpty.checkEmpty(identityType, "identityType");

    Map<String, String> param = Maps.newHashMap();
    param.put("identity", identity);
    param.put("identityType", identityType.getType());
    return userMapper.getUserByIdentity(param);
  }
  
  /**
   * 根据帐号获取用户信息，注意判断用户状态.
   *
   * @param account 帐号唯一编号：邮箱或手机
   * @param tenancyCode 租户编码
   * @param tenancyId 租户Id
   * @param withPhoneChecked 是否根据手机查询true是，false 否
   * @param status 用户启用禁用状态,null表示任意状态
   * @return 用户信息
   * @throws AppException not found or find multiple user
   * @see {@link AppConstants#STATUS_ENABLED 用户状态：启用}
   * @see {@link AppConstants#STATUS_DISABLED 用户状态：禁用}
   */
  public User getUserByAccount(String account, String tenancyCode, Long tenancyId,
      boolean withPhoneChecked, Byte status) {
    Map<String, String> map = new HashMap<String, String>();
    map.put("email", account);

    if (withPhoneChecked) {
      map.put("phone", account);
    }
    if (tenancyCode == null && tenancyId == null) {
      map.put("tenancyId", tenancyService.getTenancyIdWithCheck().toString());
    } else {
      if (tenancyId == null) {
        CheckEmpty.checkEmpty(tenancyCode, "租户code");
        map.put("tenancyCode", tenancyCode);
      } else {
        map.put("tenancyId", tenancyId.toString());
      }
    }
    if (status != null) {
      map.put("status", Integer.toString(status));
    }
    List<User> userList = userMapper.selectByEmailOrPhone(map);
    if (userList == null || userList.isEmpty()) {
      throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND,
          UniBundle.getMsg("user.login.notfound", account));
    }

    // search enable user
    int enableUserCount = 0;
    User enableUser = null;
    for (User user : userList) {
      if (user.getStatus() == AppConstants.STATUS_ENABLED) {
        enableUser = user;
        enableUserCount++;
      }
    }
    if (enableUserCount > 1) {
      throw new AppException(InfoName.LOGIN_ERROR_MULTI_USER_FOUND,
          UniBundle.getMsg("user.login.multiuser.found"));
    }

    User user = enableUser == null ? userList.get(0) : enableUser;
    // 手动设置tenancyId -- important
    CxfHeaderHolder.TENANCYID.set(user.getTenancyId());
    return user;
  }
  
  public User getUserByPrimaryKey(Long id) {
    CheckEmpty.checkEmpty(id, "userId");
    User user = userMapper.selectByPrimaryKey(id);
    if (user != null) {
      // 手动设置tenancyId important
      CxfHeaderHolder.TENANCYID.set(user.getTenancyId());
    }
    return user;
  }
  
  /**
   * 检测电话和邮箱, 不能同时为空.
   *
   * @param phone 电话号码(手机号码)
   * @param email 邮箱地址
   * @param userId 用户的主键ID
   * @throws AppException 如果检测不通过
   */
  private void checkPhoneAndEmail(String phone, String email, Long userId) {
    if (!StringUtils.hasText(phone) && !StringUtils.hasText(email)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.two.empty", "email", "phone"));
    }
    checkEmail(email, userId, false);
    checkPhone(phone, userId, false);
  }

  private void checkEmail(String email, Long userId) {
    checkEmail(email, userId, true);
  }

  private void checkEmail(String email, Long userId, boolean checkEmpty) {
    if (!StringUtils.hasText(email)) {
      if (checkEmpty) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.parameter.empty", "email"));
      }
      return;
    }
    if (!StringUtil.isEmailAddress(email)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("user.parameter.email.invalid", email));
    }
    // check duplicate email
    if (userId == null) {
      dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_EMAIL, email);
    } else {
      dataFilter.updateFieldCheck(Integer.parseInt(userId.toString()), FieldType.FIELD_TYPE_EMAIL,
          email);
    }
  }

  private void checkPhone(String phone, Long userId) {
    checkPhone(phone, userId, true);
  }

  private void checkPhone(String phone, Long userId, boolean checkEmpty) {
    if (!StringUtils.hasText(phone)) {
      if (checkEmpty) {
        throw new AppException(InfoName.VALIDATE_FAIL,
            UniBundle.getMsg("common.parameter.empty", "phone"));
      }
      return;
    }
    if (!StringUtil.isPhoneNumber(phone)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("user.parameter.phone.invalid", phone));
    }
    // check duplicate phone
    if (userId == null) {
      dataFilter.addFieldCheck(FilterType.EXSIT_DATA, FieldType.FIELD_TYPE_PHONE, phone);
    } else {
      dataFilter.updateFieldCheck(Integer.parseInt(userId.toString()), FieldType.FIELD_TYPE_PHONE,
          phone);
    }
  }

  /**
   * 检验密码是否符合要求.
   *
   * @param userId userId
   * @param password the new password
   */
  private void checkUserPwd(Long userId, String password, Boolean ignorePwdStrategyCheck) {
    CheckEmpty.checkEmpty(userId, "userId");
    // check
    if (password == null) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("common.parameter.empty", "password"));
    }

    // 判断是否忽略密码设置策略检查
    if (ignorePwdStrategyCheck != null && ignorePwdStrategyCheck) {
      return;
    }
    // 符合密码复杂度
    if (!AuthUtils.validatePasswordRule(password)) {
      throw new AppException(InfoName.VALIDATE_FAIL,
          UniBundle.getMsg("user.parameter.password.rule"));
    }

    // 不能设置过去8个月内设置过的密码
    UserPwdLogQueryParam condition = new UserPwdLogQueryParam();
    condition.setUserId(userId);
    Calendar time = Calendar.getInstance();
    time.add(Calendar.MONTH, -AppConstants.DUPLICATE_PWD_VALID_MONTH);
    condition.setCreateDateBegin(time.getTime());
    List<UserPwdLog> logs = userPwdLogMapper.queryUserPwdLogs(condition);
    if (logs == null || logs.isEmpty()) {
      return;
    }
    // check duplicate password
    for (UserPwdLog log : logs) {
      if (UniPasswordEncoder.isPasswordValid(log.getPassword(), password, log.getPasswordSalt())) {
        throw new AppException(InfoName.VALIDATE_FAIL, UniBundle
            .getMsg("user.parameter.password.duplicate", AppConstants.DUPLICATE_PWD_VALID_MONTH));
      }
    }
  }

  /**
   * 异步记录用户的密码设置记录.
   *
   * @param user info
   */
  private void asynAddUserPwdLog(final User user) {
    Assert.notNull(user);
    // 异步添加UserPwdLog
    executor.submit(new Runnable() {
      @Override
      public void run() {
        try {
          UserPwdLog log = new UserPwdLog();
          log.setUserId(user.getId());
          log.setPassword(user.getPassword());
          log.setPasswordSalt(user.getPasswordSalt());
          log.setCreateDate(new Date());
          log.setTenancyId(user.getTenancyId());
          userPwdLogMapper.insert(log);
        } catch (Exception ex) {
          log.error("failed to log add new user pwd log", ex);
        }
      }
    });
  }
}
