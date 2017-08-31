package com.dianrong.common.uniauth.client.custom.sharedomain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * 用于共享域的实现.
 *
 * @author wanglin
 */
public class ShareDomainAuthentication implements Authentication {

  private static final long serialVersionUID = -9091689502132190220L;

  /**
   * 用户的详细信息.
   */
  private Object principal;
  /**
   * 原始Authentication.
   */
  private Authentication originalAuthentication;

  /**
   * 构造函数.
   */
  public ShareDomainAuthentication(Authentication originalAuthentication, Object principal) {
    Assert.notNull(originalAuthentication);
    Assert.notNull(principal);
    this.originalAuthentication = originalAuthentication;
    this.principal = principal;
  }

  /**
   * 重写getAuthorities方法.
   */
  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    if (principal instanceof UserDetails) {
      return Collections.unmodifiableCollection(((UserDetails) principal).getAuthorities());
    }
    return Collections.unmodifiableCollection(this.originalAuthentication.getAuthorities());
  }

  @Override
  public String getName() {
    return this.originalAuthentication.getName();
  }

  @Override
  public Object getCredentials() {
    return this.originalAuthentication.getCredentials();
  }

  @Override
  public Object getDetails() {
    return this.originalAuthentication.getDetails();
  }

  @Override
  public Object getPrincipal() {
    return this.originalAuthentication.getPrincipal();
  }

  @Override
  public boolean isAuthenticated() {
    return this.originalAuthentication.isAuthenticated();
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.originalAuthentication.setAuthenticated(isAuthenticated);
  }
}
