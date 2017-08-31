package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.util.Assert;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 支持JWT的AuthenticationToken.
 * 
 * @author wanglin
 *
 */
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -8443157279215749460L;

  private UserDetails userDetails;

  private Object credentials;

  public JWTAuthenticationToken(Collection<? extends GrantedAuthority> authorities, UserDetails userDetails, Object credentials) {
    super(authorities);
    Assert.notNull(userDetails);
    this.userDetails = userDetails;
    this.credentials = credentials;
    super.setAuthenticated(true);
  }
  @Override
  public Object getCredentials() {
    return this.credentials;
  }

  @Override
  public Object getPrincipal() {
    return this.userDetails;
  }
}
