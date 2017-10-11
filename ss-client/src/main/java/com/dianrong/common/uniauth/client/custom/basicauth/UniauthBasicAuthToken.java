package com.dianrong.common.uniauth.client.custom.basicauth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 用于进行Basic Auth身份信息验证的token.
 * 
 * @author wanglin
 */
public class UniauthBasicAuthToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -147418735497216453L;

  /**
   * 用户账号.
   */
  private final String tenancyCode;

  private final String account;

  private final String password;

  private final String ip;


  public UniauthBasicAuthToken(String tenancyCode, String account, String password, String ip) {
    super(null);
    Assert.notNull(tenancyCode, "tenancyCode must not be null");
    Assert.notNull(account, "account must not be null");
    Assert.notNull(ip, "ip must not be null");
    this.tenancyCode = tenancyCode;
    this.account = account;
    this.password = password;
    this.ip = ip;
    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return "PROTECTED";
  }

  @Override
  public Object getPrincipal() {
    return this.account;
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }

  public String getIp() {
    return ip;
  }
}
