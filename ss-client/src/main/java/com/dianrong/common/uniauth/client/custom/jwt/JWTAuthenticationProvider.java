package com.dianrong.common.uniauth.client.custom.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.dianrong.common.uniauth.client.custom.model.StatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.client.custom.multitenancy.MultiTenancyUserDetailsService;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 用于处理JWT信息登陆的Provider.
 * 
 * @author wanglin
 *
 */
public class JWTAuthenticationProvider implements AuthenticationProvider {

  private MultiTenancyUserDetailsService userDetailsService;

  public JWTAuthenticationProvider(MultiTenancyUserDetailsService userDetailsService) {
    Assert.notNull(userDetailsService);
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    UniauthIdentityToken uniauthIdentityToken = (UniauthIdentityToken) authentication;
    UserDetails userDetails = userDetailsService.loadUserByUsername(
        uniauthIdentityToken.getIdentity(), uniauthIdentityToken.getTenancyId());
    return new StatelessAuthenticationSuccessToken(userDetails.getAuthorities(), userDetails,
        userDetails.getPassword());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UniauthIdentityToken.class.isAssignableFrom(authentication);
  }
}
