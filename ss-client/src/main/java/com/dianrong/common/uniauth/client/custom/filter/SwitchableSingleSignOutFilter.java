package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.security.web.access.SwitchControl;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 可开关控制的SingleSignOutFilter.
 * 
 * @author wanglin
 *
 */

@Slf4j
public class SwitchableSingleSignOutFilter extends OncePerRequestFilter implements SwitchControl {
  private SingleSignOutFilter originalFilter;

  /**
   * 当前采用的验证方式. 默认使用的是CAS.
   */
  private AuthenticationType authenticationType = AuthenticationType.CAS;

  public SwitchableSingleSignOutFilter(SingleSignOutFilter originalFilter) {
    Assert.notNull(originalFilter);
    this.originalFilter = originalFilter;
  }

  @Override
  public boolean isOn() {
    return this.authenticationType.equals(AuthenticationType.CAS);
  }

  public AuthenticationType getAuthenticationType() {
    return authenticationType;
  }

  public void setAuthenticationType(AuthenticationType authenticationType) {
    Assert.notNull(authenticationType);
    log.info("Set current authenticationType is {}", authenticationType);
    this.authenticationType = authenticationType;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (isOn()) {
      this.originalFilter.doFilter(request, response, filterChain);
    } else {
      filterChain.doFilter(request, response);
    }
  }
}
