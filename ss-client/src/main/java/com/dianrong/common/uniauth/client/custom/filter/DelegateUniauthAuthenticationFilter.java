package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.auth.UniauthAuthenticationFilterChain;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.OrderComparator;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Uniauth提供的身份认证Filter.
 */

@Slf4j
public class DelegateUniauthAuthenticationFilter extends OncePerRequestFilter {

  private List<UniauthAbstractAuthenticationFilter> authenticationFilterList;

  // 传入用户配置的AuthenticationType类型列表.
  private final List<AuthenticationType> authenticationTypeList;

  public DelegateUniauthAuthenticationFilter(
      List<UniauthAbstractAuthenticationFilter> authenticationFilterList, List<AuthenticationType> authenticationTypeList) {
    setAuthenticationFilterList(authenticationFilterList);
    Assert.isTrue(!CollectionUtils.isEmpty(authenticationTypeList), "authenticationTypeList can not be empty!");
    this.authenticationTypeList = authenticationTypeList;
  }

  public List<UniauthAbstractAuthenticationFilter> getAuthenticationFilterList() {
    return authenticationFilterList;
  }

  public void setAuthenticationFilterList(
      List<UniauthAbstractAuthenticationFilter> authenticationFilterList) {
    Assert.notNull(authenticationFilterList, "authenticationFilterList must not be null.");
    this.authenticationFilterList = authenticationFilterList;
  }

  public void addAuthenticationFilterList(
      UniauthAbstractAuthenticationFilter authenticationFilter) {
    Assert.notNull(authenticationFilter,
        "AuthenticationFilter can not be null");
    this.authenticationFilterList.add(authenticationFilter);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    if (this.authenticationFilterList.isEmpty()) {
      log.debug(UrlUtils.buildRequestUrl(request) + " has an empty filter list");
      filterChain.doFilter(request, response);
      return;
    }
    UniauthAuthenticationFilterChain vfc = new UniauthAuthenticationFilterChain(filterChain,
        this.authenticationFilterList);
    vfc.doFilter(request, response);
  }

  @Override
  public void afterPropertiesSet() throws ServletException {
    super.afterPropertiesSet();
    // 根据配置,筛选出起作用的AuthenticationFilter
    List<UniauthAbstractAuthenticationFilter> enableFilterList = new ArrayList<>(this.authenticationFilterList.size());
    for (UniauthAbstractAuthenticationFilter uaAuthenticationFilter : this.authenticationFilterList) {
      for (AuthenticationType authenticationType : this.authenticationTypeList) {
        if (authenticationType.isSupported(uaAuthenticationFilter.authenticationType())) {
          enableFilterList.add(uaAuthenticationFilter);
          break;
        }
      }
    }
    // 排序
    Collections.sort(enableFilterList, OrderComparator.INSTANCE);
    this.authenticationFilterList = enableFilterList;
  }
}
