package com.dianrong.common.uniauth.client.custom.basicauth;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.dianrong.common.uniauth.client.custom.model.StatelessAuthenticationSuccessToken;

public class BasicAuthStatelessAuthenticationSuccessToken
    extends StatelessAuthenticationSuccessToken {

  /**
   * 用户账号.
   */
  private final String tenancyCode;

  private final String account;

  public BasicAuthStatelessAuthenticationSuccessToken(
      Collection<? extends GrantedAuthority> authorities, UserDetails userDetails,
      Object credentials, String tenancyCode, String account) {
    super(authorities, userDetails, credentials);
    this.tenancyCode = tenancyCode;
    this.account = account;
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public String getAccount() {
    return account;
  }
}
