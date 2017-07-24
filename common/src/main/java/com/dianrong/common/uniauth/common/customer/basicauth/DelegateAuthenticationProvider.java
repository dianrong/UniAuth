package com.dianrong.common.uniauth.common.customer.basicauth;

import static com.dianrong.common.uniauth.common.customer.basicauth.mode.Mode.ROLE_CODE;
import static com.dianrong.common.uniauth.common.customer.basicauth.mode.PermissionType.DOMAIN;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.customer.basicauth.cache.service.CacheMapServiceImpl;
import com.dianrong.common.uniauth.common.customer.basicauth.cache.service.CacheService;
import com.dianrong.common.uniauth.common.customer.basicauth.factory.ModeFactory;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.Mode;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.PermissionType;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Created by denghb on 6/13/17.
 */
@Slf4j
public class DelegateAuthenticationProvider implements AuthenticationProvider {

  private UniClientFacade uniClientFacade;

  /**
   * 默认的租户编码:Dianrong.
   */
  private String tenancyCode = AppConstants.DEFAULT_TANANCY_CODE;

  /**
   * 默认的权限模型:Role_Code.
   */
  private Mode mode = ROLE_CODE;

  /**
   * 默认的权限类型:Domain.
   */
  private PermissionType permissionType = DOMAIN;

  /**
   * 默认处理的域编码:Techops.
   */
  private String domainCode = AppConstants.DOMAIN_CODE_TECHOPS;

  // 缓存的默认实现.
  private CacheService cacheService = new CacheMapServiceImpl();

  public DelegateAuthenticationProvider setCacheService(@NonNull CacheService cacheService) {
    this.cacheService = cacheService;
    return this;
  }

  public DelegateAuthenticationProvider(@NonNull UniClientFacade uniClientFacade) {
    this.uniClientFacade = uniClientFacade;
  }

  public DelegateAuthenticationProvider setTenancyCode(@NonNull String tenancyCode) {
    this.tenancyCode = tenancyCode;
    return this;
  }

  public DelegateAuthenticationProvider setMode(@NonNull Mode mode) {
    this.mode = mode;
    return this;
  }

  public DelegateAuthenticationProvider setPermissionType(@NonNull PermissionType permissionType) {
    this.permissionType = permissionType;
    return this;
  }

  public DelegateAuthenticationProvider setDomainDefine(@NonNull String domainCode) {
    this.domainCode = domainCode;
    return this;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userName = authentication.getName();
    String password = (String) authentication.getCredentials();
    Assert.notNull(userName);
    Assert.notNull(password);
    String identity = getIdentityMd5Key(userName, password);
    Object token = cacheService.getDataFromCache(identity);
    if (token instanceof UsernamePasswordAuthenticationToken) {
      log.debug("get cache of userName:{}", userName);
      return (UsernamePasswordAuthenticationToken) token;
    }
    // 重新走登陆流程.
    // Get remote ip address
    String remoteAddress = null;
    Object details = authentication.getDetails();
    if (details instanceof WebAuthenticationDetails) {
      WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
      remoteAddress = webDetails.getRemoteAddress();
    }
    LoginParam loginParam = new LoginParam();
    loginParam.setAccount(userName);
    loginParam.setPassword(password);
    loginParam.setTenancyCode(tenancyCode);
    loginParam.setIp(remoteAddress);
    Response<UserDto> response = uniClientFacade.getUserResource().login(loginParam);
    List<Info> infoList = response.getInfo();
    if (infoList != null && !infoList.isEmpty()) {
      Info info = infoList.get(0);
      InfoName infoName = info.getName();

      try {
        if (InfoName.LOGIN_ERROR_USER_NOT_FOUND.equals(infoName)) {
          log.debug("User " + userName + " not found in db");
          throw new UsernameNotFoundException("User " + userName + " not found in db.");
        }
        if (InfoName.LOGIN_ERROR.equals(infoName)) {
          log.debug("User " + userName + " password not match.");
          throw new BadCredentialsException("User " + userName + " password not match.");
        } else if (InfoName.LOGIN_ERROR_MULTI_USER_FOUND.equals(infoName)) {
          log.debug("Multiple " + userName + " found in db.");
          throw new BadCredentialsException("Multiple " + userName + " found in db.");
        } else if (InfoName.LOGIN_ERROR_STATUS_1.equals(infoName)) {
          log.debug(userName + " disabled(status == 1) in db.");
          throw new DisabledException(userName + " disabled(status == 1) in db.");
        } else if (InfoName.LOGIN_ERROR_EXCEED_MAX_FAIL_COUNT.equals(infoName)) {
          log.debug(userName + " locked due to too many failed login attempts.");
          throw new LockedException(userName + " locked due to too many failed login attempts.");
        } else if (InfoName.LOGIN_ERROR_IPA_TOO_MANY_FAILED.equals(infoName)) {
          log.debug("IPA Account " + userName + " login failed too many times.");
          throw new BadCredentialsException(
              "IPA Account " + userName + " login failed too many times.");
        } else if (InfoName.LOGIN_ERROR_NEW_USER.equals(infoName)) {
          log.debug("Newly added user, must modify password first.");
          throw new CredentialsExpiredException("Newly added user, must modify password first.");
        } else if (InfoName.LOGIN_ERROR_EXCEED_MAX_PASSWORD_VALID_MONTH.equals(infoName)) {
          log.debug("Need modify password due to password policy.");
          throw new CredentialsExpiredException("Need modify password due to password policy.");
        } else {
          log.debug(userName + "/" + password + "not matched.");
          throw new BadCredentialsException(userName + "/" + password + "not matched.");
        }
      } catch (Exception ex) {
        log.error("username + password login, error :" + ex);
        throw ex;
      }
    }

    // 首先从缓存中拿数据
    Response<UserDetailDto> responseDetail =
        uniClientFacade.getUserResource().getUserDetailInfo(loginParam);
    UserDetailDto userDetailDto = responseDetail.getData();
    ModeFactory modeFactory = new ModeFactory();
    ArrayList<SimpleGrantedAuthority> simpleGrantedAuthorityArrayList =
        modeFactory.getHandlerBean(mode).handle(userDetailDto, domainCode, permissionType);

    // login success, cache login result
    UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
        userName, password, simpleGrantedAuthorityArrayList);
    cacheService.setDataToCache(identity, loginToken);
    return loginToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }

  /**
   * 将用户名和密码通过Md5生成一个标识Key.<br>
   * 
   * @return Md5(UserName + Password).
   */
  private String getIdentityMd5Key(String userName, String password) {
    String originStr = userName.trim() + password.trim();
    try {
      return StringUtil.md5(originStr);
    } catch (UnsupportedEncodingException e) {
      log.error("not supported encoding type!", e);
      return null;
    }
  }
}
