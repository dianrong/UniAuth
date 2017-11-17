package com.dianrong.common.uniauth.server.service;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.ThirdAccountType;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.InnerStringUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.ldap.ipa.dao.UserDao;
import com.dianrong.common.uniauth.server.ldap.ipa.entity.User;
import com.dianrong.common.uniauth.server.service.cache.TenancyCache;
import com.dianrong.common.uniauth.server.service.common.TenancyBasedService;
import com.dianrong.common.uniauth.server.service.multidata.UserAuthentication;
import com.dianrong.common.uniauth.server.service.support.IpaLoginForDRCheckSwitchControl;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.UniBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.OperationNotSupportedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 处理IPA相关信息.
 *
 * @author wanglin
 */
@Service
@Slf4j
public class IpaService extends TenancyBasedService implements UserAuthentication {

  @Autowired
  private UserDao userDao;

  @Autowired
  private UserThirdAccountService serThirdAccountService;

  @Autowired
  private UserService userService;

  @Autowired
  private IpaLoginForDRCheckSwitchControl ipaLoginForDRCheckSwitchControl;

  @Autowired
  private TenancyCache tenancyCache;

  /**
   * IPA账号的登陆.
   */
  public UserDto login(LoginParam loginParam) {
    checkTenancy(loginParam);
    try {
      userDao.authenticate(loginParam.getAccount(), loginParam.getPassword());
    } catch (AuthenticationException ae) {
      log.warn("IPA login, account {0}, password not match", ae);
      throw new AppException(InfoName.LOGIN_ERROR, UniBundle.getMsg("user.login.error"));
    } catch (EmptyResultDataAccessException eda) {
      log.warn("IPA login, account {0} not exsits", eda);
      throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND,
          UniBundle.getMsg("user.login.notfound", loginParam.getAccount()));
    } catch (OperationNotSupportedException ose) {
      log.warn("IPA login, account {0} too many times login failed", ose);
      throw new AppException(InfoName.LOGIN_ERROR_IPA_TOO_MANY_FAILED,
          UniBundle.getMsg("user.login.account.lock"));
    } catch (Exception e) {
      log.warn("IPA login, account {0} login failed", e);
      throw new AppException(InfoName.LOGIN_ERROR, UniBundle.getMsg("user.login.error"));
    }
    // load user basic information
    User user = userDao.getUserByAccount(loginParam.getAccount());
    if (StringUtils.hasText(user.getEmail())) {
      UserDto uniauthUserDto = getUniauthUserInfo(user, loginParam.getAccount());
      if (uniauthUserDto != null) {
        return uniauthUserDto;
      }
    }
    // 没有关联的Uniauth账号
    UserDto ipaDto = BeanConverter.convert(user);
    ipaDto
        .setTenancyId(StringUtil.translateObjectToInteger(tenancyService.getTenancyIdWithCheck()));
    // Third Account info
    ipaDto.setThirdAccountInfo(
        userService.thirdAccountInfo(ipaDto.getId(), loginParam.getIncludeThirdAccount()));
    return ipaDto;
  }

  /**
   * 判断传入的租户是否通过.
   */
  private void checkTenancy(LoginParam loginParam) {
    if (ipaLoginForDRCheckSwitchControl.isOn()) {
      return;
    }
    Long tenancyId = tenancyService.getTenancyIdWithCheck();
    Long dianrongTenancyId = tenancyCache.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE)
        .getId();
    // 只能是点融的账号才能通过ipa账号登录
    if (!tenancyId.equals(dianrongTenancyId)) {
      throw new AppException(InfoName.LOGIN_ERROR_USER_NOT_FOUND,
          UniBundle.getMsg("user.login.notfound", loginParam.getAccount()));
    }
  }

  /**
   * 根据Email从Uniauth的数据源中获取Uniauth用户信息. 1 如果不存在,则创建一个,并关联IPA账号和Uniauth账号 2 如果存在则直接返回对应的账号.
   */
  private UserDto getUniauthUserInfo(User ipaUser, String ipaAccount) {
    UserDto user = serThirdAccountService.queryUserByThirdAccount(ipaAccount, ThirdAccountType.IPA);
    // 创建新用户 并关联
    if (user == null) {
      user = serThirdAccountService.createNewUserAndRelateThirdAccount(ipaUser.getDisplayName(),
          ipaUser.getPhone(), ipaUser.getEmail(), ipaAccount, ThirdAccountType.IPA);
    }
    // check user lock status
    if (user.getFailCount() >= AppConstants.MAX_AUTH_FAIL_COUNT) {
      throw new AppException(InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT,
          UniBundle.getMsg("user.login.account.lock"));
    }
    // 用户如果被禁用,则报错
    if (user.getStatus() != AppConstants.STATUS_ENABLED) {
      throw new AppException(InfoName.LOGIN_ERROR_STATUS_1,
          UniBundle.getMsg("user.login.status.lock"));
    }

    return user;
  }

  @Override
  public int getOrder() {
    return -10;
  }

  @Override
  public boolean supported(LoginParam loginParam) {
    if (loginParam.getThirdAccountType() != null) {
      return loginParam.getThirdAccountType().equals(ThirdAccountType.IPA);
    }
    return InnerStringUtil.isIpaAccount(loginParam.getAccount());
  }
}
