package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.ThirdAccountType;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.data.entity.UserExample;
import com.dianrong.common.uniauth.server.data.entity.UserThirdAccount;
import com.dianrong.common.uniauth.server.data.entity.UserThirdAccountExample;
import com.dianrong.common.uniauth.server.data.mapper.UserMapper;
import com.dianrong.common.uniauth.server.data.mapper.UserThirdAccountMapper;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;
import com.dianrong.common.uniauth.server.util.UniBundle;

import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 用户和第三方账号的关联处理service.
 */
@Slf4j
@Service
public class UserThirdAccountService extends TenancyBasedService {

  @Autowired
  private UserThirdAccountMapper userThirdAccountMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private UserService userService;

  /**
   * 根据第三方账号获取对应的Uniauth账号.
   * 
   * @return 如果找不到,则返回null
   */
  public UserDto queryUserByThirdAccount(String thirdAccount, ThirdAccountType type) {
    CheckEmpty.checkEmpty(thirdAccount, "thirdAccount");
    CheckEmpty.checkEmpty(type, "thirdAccountType");
    UserThirdAccountExample uaExample = new UserThirdAccountExample();
    uaExample.createCriteria().andThirdAccountEqualTo(thirdAccount).andTypeEqualTo(type.toString())
        .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
    List<UserThirdAccount> userThirdAccountList = userThirdAccountMapper.selectByExample(uaExample);
    if (userThirdAccountList == null || userThirdAccountList.isEmpty()) {
      return null;
    }
    // get user id
    Long userId = userThirdAccountList.get(0).getUserId();
    User user = userMapper.selectByPrimaryKey(userId);
    return BeanConverter.convert(user);
  }

  /**
   * 创建用户,并且关联用户与第三方账号.
   * 
   * @return 新创建的用户.
   */
  @Transactional
  public UserDto createNewUserAndRelateThirdAccount(String name, String phone, String email,
      String thirdAccount, ThirdAccountType type) {
    if (!StringUtils.hasText(phone) && !StringUtils.hasText(email)) {
      throw new AppException(InfoName.BAD_REQUEST,
          UniBundle.getMsg("common.parameter.empty", "account"));
    }
    CheckEmpty.checkEmpty(thirdAccount, "thirdAccount");
    CheckEmpty.checkEmpty(type, "thirdAccountType");

    User user = null;
    // check Email
    if (StringUtils.hasText(email)) {
      UserExample emailExample = new UserExample();
      emailExample.createCriteria().andEmailEqualTo(email.trim())
          .andStatusEqualTo(AppConstants.STATUS_ENABLED)
          .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
      List<User> uniauthUsers = userMapper.selectByExample(emailExample);
      if (uniauthUsers != null && !uniauthUsers.isEmpty()) {
        user = uniauthUsers.get(0);
      }
    }

    String insertPhone = phone;
    // check Phone
    if (StringUtils.hasText(phone)) {
      if (user != null && phone.trim().equalsIgnoreCase(user.getPhone())) {
        // 用户的Email和Phone在Uniauth和IPA中是统一的. 不用再继续根据phone去匹配用户了.
      } else {
        UserExample phoneExample = new UserExample();
        phoneExample.createCriteria().andPhoneEqualTo(phone.trim())
            .andStatusEqualTo(AppConstants.STATUS_ENABLED)
            .andTenancyIdEqualTo(tenancyService.getTenancyIdWithCheck());
        List<User> uniauthUsers = userMapper.selectByExample(phoneExample);
        if (uniauthUsers != null && !uniauthUsers.isEmpty()) {
          if (user != null) {
            // 已经根据Email找到了用户,所以忽略根据phone找到的用户关系.
            insertPhone = null;
          } else {
            // 使用根据phone找到的用户,与第三方账号进行关联
            user = uniauthUsers.get(0);
          }
        }
      }
    }

    Long userId = null;
    UserDto userDto = null;
    if (user == null) {
      userDto = userService.addNewUser(name, insertPhone, email);
      log.info("{} login, create a new Uniauth user:ThirdAccout: {}, Name:{}, Email:{}, Phone:{}",
          type.toString(), thirdAccount, name, email, phone);
      userId = userDto.getId();
    } else {
      userId = user.getId();
    }
    // 关联用户和第三方账号
    UserThirdAccount userThirdAccount = new UserThirdAccount();
    userThirdAccount.setTenancyId(tenancyService.getTenancyIdWithCheck());
    userThirdAccount.setCreateDate(new Date());
    userThirdAccount.setThirdAccount(thirdAccount);
    userThirdAccount.setType(type.toString());
    userThirdAccount.setUserId(userId);
    userThirdAccountMapper.insert(userThirdAccount);
    return userDto != null ? userDto : BeanConverter.convert(user);
  }
}
