package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.OrderComparator;

/**
 * 支持所有的验证方式的一种实现.
 * 
 * @author wanglin
 */

public class AllAuthenticationFilter
    implements UniauthAuthenticationFilter, ApplicationContextAware, InitializingBean {

  private Set<UniauthAuthenticationFilter> authenticationFilters = Sets.newHashSet();

  private ApplicationContext applicationContext;

  @Override
  public AuthenticationType authenticationType() {
    return AuthenticationType.ALL;
  }

  @Override
  public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    return true;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    for (UniauthAuthenticationFilter authenticationFilter : authenticationFilters) {
      if (authenticationFilter.requiresAuthentication(request, response)) {
        authenticationFilter.doFilter(request, response, chain);
        return;
      }
    }
    chain.doFilter(request, response);
  }

  public void init() {
    Comparator<Object> comparator = OrderComparator.INSTANCE;
    List<UniauthAuthenticationFilter> allAuthenticationFilters =
        new ArrayList<>(this.authenticationFilters);
    // 按照Ordered排序.
    Collections.sort(allAuthenticationFilters, comparator);
    this.authenticationFilters = new LinkedHashSet<>(allAuthenticationFilters);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    String[] names = this.applicationContext.getBeanNamesForType(UniauthAuthenticationFilter.class,
        false, false);
    for (String name : names) {
      UniauthAuthenticationFilter authenticationFilter =
          (UniauthAuthenticationFilter) this.applicationContext.getBean(name);
      if (authenticationFilter instanceof AllAuthenticationFilter) {
        // 排除自己
        continue;
      }
      this.authenticationFilters.add(authenticationFilter);
    }

    // Initial
    init();
  }

  @Override
  public int getOrder() {
    return 1000;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}

  public Set<UniauthAuthenticationFilter> getAuthenticationFilters() {
    return Collections.unmodifiableSet(this.authenticationFilters);
  }

  public void setAuthenticationFilters(
      Collection<UniauthAuthenticationFilter> authenticationFilters) {
    Assert.notNull(authenticationFilters);
    this.authenticationFilters.addAll(authenticationFilters);
  }
}
