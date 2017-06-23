package com.dianrong.common.uniauth.client.custom.model;

import java.util.Collection;
import java.util.Collections;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * 用于共享域的实现.
 *
 * @author wanglin
 */
public class ShareDomainAuthentication extends CasAuthenticationToken {

  private static final long serialVersionUID = -9091689502132190220L;

  private UserDetails userDetails;

  /**
   * 构造函数.
   */
  public ShareDomainAuthentication(String key, Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities, UserDetails userDetails,
      Assertion assertion) {
    super(key, principal, credentials, authorities, userDetails, assertion);
    Assert.notNull(userDetails);
    this.userDetails = userDetails;
  }

  @Override
  public Collection<GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(userDetails.getAuthorities());
  }
}
