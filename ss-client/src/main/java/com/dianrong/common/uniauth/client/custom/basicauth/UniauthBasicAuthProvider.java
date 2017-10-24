package com.dianrong.common.uniauth.client.custom.basicauth;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.dianrong.common.uniauth.client.custom.multitenancy.MultiTenancyUserLoginService;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 处理Basic auth的信息认证的Provider.
 * 
 * @author wanglin
 *
 */
public class UniauthBasicAuthProvider implements AuthenticationProvider {

  private MultiTenancyUserLoginService multiTenancyUserLoginService;

  public UniauthBasicAuthProvider(MultiTenancyUserLoginService multiTenancyUserLoginService) {
    Assert.notNull(multiTenancyUserLoginService, "MultiTenancyUserLoginService can not be null");
    this.multiTenancyUserLoginService = multiTenancyUserLoginService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UniauthBasicAuthToken basicAuthToken = (UniauthBasicAuthToken) authentication;
    UserDetails userDetails =
        multiTenancyUserLoginService.loadLoginUserDetails(basicAuthToken.getTenancyCode(),
            basicAuthToken.getAccount(), basicAuthToken.getPassword(), basicAuthToken.getIp());
    return new BasicAuthStatelessAuthenticationSuccessToken(userDetails.getAuthorities(),
        userDetails, userDetails.getPassword(), basicAuthToken.getTenancyCode(),
        basicAuthToken.getAccount());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UniauthBasicAuthToken.class.isAssignableFrom(authentication);
  }
}
