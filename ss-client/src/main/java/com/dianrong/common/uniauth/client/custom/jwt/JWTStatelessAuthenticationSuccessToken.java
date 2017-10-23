package com.dianrong.common.uniauth.client.custom.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dianrong.common.uniauth.client.custom.auth.StatelessAuthenticationSuccessToken;

public class JWTStatelessAuthenticationSuccessToken extends StatelessAuthenticationSuccessToken {

  /**
   * 用户账号.
   */
  private final String identity;

  /**
   * 租户id.
   */
  private final Long tenancyId;

  public JWTStatelessAuthenticationSuccessToken(Collection<? extends GrantedAuthority> authorities,
      UserDetails userDetails, Object credentials, String identity, Long tenancyId) {
    super(authorities, userDetails, credentials);
    this.identity = identity;
    this.tenancyId = tenancyId;
  }

  public String getIdentity() {
    return identity;
  }

  public Long getTenancyId() {
    return tenancyId;
  }
}
