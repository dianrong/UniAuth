package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.util.Assert;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 用于存储Uniauth的登陆账号信息.
 * 
 * @author wanglin
 */
public class UniauthIdentityToken extends AbstractAuthenticationToken {

  private static final long serialVersionUID = -147418735497216453L;

  /**
   * 用户账号.
   */
  private final String identity;
  
  /**
   * 租户id.
   */
  private final Long tenancyId;
  
  public UniauthIdentityToken(String identity, Long tenancyId) {
    super(null);
    Assert.notNull(identity);
    Assert.notNull(tenancyId);
    this.identity = identity;
    this.tenancyId = tenancyId;
    super.setAuthenticated(false);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  @Override
  public Object getPrincipal() {
    return null;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  public String getIdentity() {
    return identity;
  }
}
