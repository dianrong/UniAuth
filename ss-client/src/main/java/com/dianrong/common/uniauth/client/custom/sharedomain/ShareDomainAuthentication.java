package com.dianrong.common.uniauth.client.custom.sharedomain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.client.custom.model.ItemBox;

/**
 * 用于共享域的实现.
 *
 * @author wanglin
 */
public class ShareDomainAuthentication extends ItemBox<Authentication> implements Authentication {

  private static final long serialVersionUID = -9091689502132190220L;

  /**
   * 用户的详细信息.
   */
  private final Object principal;

  /**
   * 构造函数.
   */
  public ShareDomainAuthentication(Authentication originalAuthentication, Object principal) {
    super(originalAuthentication);
    Assert.notNull(originalAuthentication);
    Assert.notNull(principal);
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
    return Collections.unmodifiableCollection(super.getItem().getAuthorities());
  }

  @Override
  public String getName() {
    return super.getItem().getName();
  }

  @Override
  public Object getCredentials() {
    return super.getItem().getCredentials();
  }

  @Override
  public Object getDetails() {
    return super.getItem().getDetails();
  }

  @Override
  public Object getPrincipal() {
    return super.getItem().getPrincipal();
  }

  @Override
  public boolean isAuthenticated() {
    return super.getItem().isAuthenticated();
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    super.getItem().setAuthenticated(isAuthenticated);
  }
}
