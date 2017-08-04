package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.client.custom.model.UniauthIdentityToken;
import com.dianrong.common.uniauth.client.custom.multitenancy.MultiTenancyUserDetailsService;
import com.dianrong.common.uniauth.common.util.Assert;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

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
    return new JWTAuthenticationToken(userDetails.getAuthorities(), userDetails,
        userDetails.getPassword());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UniauthIdentityToken.class.isAssignableFrom(authentication);
  }
}
