package com.dianrong.common.uniauth.cas.service;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.IpaAccountLoginFailedTooManyTimesException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.Assert;
import java.util.List;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.inspektr.common.web.ClientInfo;
import org.jasig.inspektr.common.web.ClientInfoHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户登陆相关的处理Service.
 *
 * @author wanglin
 */

@Slf4j
@Service
public class UserLoginService extends BaseService {

  @Autowired
  private UniClientFacade uniClientFacade;

  /**
   * 根据账号,密码,租户编码进行登陆操作.
   *
   * @param identity 账号信息.
   * @param password 密码.
   * @param tenancyCode 租户编码.
   * @return 登陆成功返回的用户信息.
   * @throws LoginException 登陆失败.
   */
  public UserDto login(String identity, String password, String tenancyCode) throws LoginException {
    LoginParam loginParam = new LoginParam();
    loginParam.setAccount(identity);
    loginParam.setPassword(password);
    loginParam.setTenancyCode(tenancyCode);
    ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
    if (clientInfo != null) {
      String clientIp = clientInfo.getClientIpAddress();
      loginParam.setIp(clientIp);
    }

    Response<UserDto> response = uniClientFacade.getUserResource().login(loginParam);
    List<Info> infoList = response.getInfo();

    if (infoList != null && !infoList.isEmpty()) {
      Info info = infoList.get(0);
      InfoName infoName = info.getName();
      try {
        if (InfoName.LOGIN_ERROR_USER_NOT_FOUND.equals(infoName)) {
          throw new AccountNotFoundException("User " + identity + " not found in db.");
        }

        if (InfoName.LOGIN_ERROR.equals(infoName)) {
          throw new UserPasswordNotMatchException("User " + identity + " password not match.");
        }

        if (InfoName.LOGIN_ERROR_MULTI_USER_FOUND.equals(infoName)) {
          throw new MultiUsersFoundException("Multiple " + identity + " found in db.");
        }

        if (InfoName.LOGIN_ERROR_STATUS_1.equals(infoName)) {
          throw new AccountDisabledException(identity + " disabled(status == 1) in db.");
        }

        if (InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT.equals(infoName)) {
          throw new AccountLockedException(
              identity + " locked due to too many failed login attempts.");
        }

        if (InfoName.LOGIN_ERROR_IPA_TOO_MANY_FAILED.equals(infoName)) {
          throw new IpaAccountLoginFailedTooManyTimesException(
              "IPA Account " + identity + " login failed too many times.");
        }

        if (InfoName.LOGIN_ERROR_NEW_USER.equals(infoName)) {
          throw new FreshUserException("Newly added user, must modify password first.");
        }

        if (InfoName.LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH.equals(infoName)) {
          throw new CredentialExpiredException("Need modify password due to password policy.");
        }

        throw new FailedLoginException(identity + "/" + password + "not matched.");
      } catch (Exception ex) {
        log.error("username + password login, error :" + ex);
        throw ex;
      }
    }
    return response.getData();
  }

  /**
   * 根据用户id获取用户的详细信息.
   **/
  public UserDto login(Long userId) throws LoginException {
    Assert.notNull(userId, "userId can not be null");
    UserParam userParam = new UserParam();
    userParam.setId(userId);
    Response<UserDto> response = uniClientFacade.getUserResource().getUserById(userParam);
    if (!CollectionUtils.isEmpty(response.getInfo())) {
      throw new FailedLoginException("User " + userId + " login failed");
    }
    if (response.getData() == null) {
      throw new AccountNotFoundException("User " + userId + " not found in db.");
    }
    return response.getData();
  }
}
