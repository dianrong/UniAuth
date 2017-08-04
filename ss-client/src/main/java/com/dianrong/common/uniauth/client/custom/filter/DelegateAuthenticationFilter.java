package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.filter.GenericFilterBean;

/**
 * Uniauth中的AuthenticationFilter的代理类. <br>
 * 用于判定采用JWT验证还是CAS验证.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class DelegateAuthenticationFilter extends GenericFilterBean {

  /**
   * 验证的方式,默认采用CAS的方式验证.
   */
  private AuthenticationType authenticationType = AuthenticationType.CAS;

  /**
   * AuthenticationFilter实现集合..
   */
  private Set<UniauthAuthenticationFilter> authenticationFilters = Sets.newHashSet();


  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    UniauthAuthenticationFilter destFilter = null;
    for (UniauthAuthenticationFilter ua : authenticationFilters) {
      if (this.authenticationType.equals(ua.authenticationType())) {
        destFilter = ua;
        break;
      }
    }
    if (destFilter == null) {
      log.warn("Do not set any AuthenticationFilter, so just ignore authentication step!");
      chain.doFilter(request, response);
    } else {
      destFilter.doFilter(request, response, chain);
    }
  }

  public AuthenticationType getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(AuthenticationType authenticationType) {
    Assert.notNull(authenticationType);
    this.authenticationType = authenticationType;
  }

  public Set<UniauthAuthenticationFilter> getAuthenticationFilters() {
    return Collections.unmodifiableSet(authenticationFilters);
  }

  public void addAuthenticationFilter(UniauthAuthenticationFilter authenticationFilter) {
    Assert.notNull(authenticationFilters);
    this.authenticationFilters.add(authenticationFilter);
  }

  public void setAuthenticationFilters(Set<UniauthAuthenticationFilter> authenticationFilters) {
    Assert.notNull(authenticationFilters);
    this.authenticationFilters = authenticationFilters;
  }
}
