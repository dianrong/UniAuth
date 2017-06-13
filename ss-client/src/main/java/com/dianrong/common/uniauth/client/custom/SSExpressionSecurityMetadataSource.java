package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.exp.UserNotLoginException;
import com.dianrong.common.uniauth.client.support.PatternMatchMost;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

@Slf4j
public class SSExpressionSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

  private Map<RequestMatcher, Collection<ConfigAttribute>> originRequestMap;
  private Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> configedRequestMap;

  /**
   * 构造函数.
   */
  public SSExpressionSecurityMetadataSource(
      LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> originRequestMap,
      Map<Long, LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>> configedRequestMap) {
    Assert.notNull(originRequestMap);
    Assert.notNull(configedRequestMap);
    this.originRequestMap = originRequestMap;
    this.configedRequestMap = configedRequestMap;
  }

  @Override
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
    for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : originRequestMap
        .entrySet()) {
      allAttributes.addAll(entry.getValue());
    }
    try {
      Long tenancyId = LoginUserInfoHolder.getCurrentLoginUserTenancyId();
      LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> configed =
          configedRequestMap.get(tenancyId);
      if (configed != null) {
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : configed.entrySet()) {
          allAttributes.addAll(entry.getValue());
        }
      }
    } catch (UserNotLoginException ex) {
      log.warn("user not login, when call getAllConfigAttributes");
    }
    return allAttributes;
  }

  @Override
  public Collection<ConfigAttribute> getAttributes(Object object) {
    Map<RequestMatcher, Collection<ConfigAttribute>> allMatchedMap =
        new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
    final HttpServletRequest request = ((FilterInvocation) object).getRequest();
    for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : originRequestMap
        .entrySet()) {
      if (entry.getKey().matches(request)) {
        allMatchedMap.put(entry.getKey(), entry.getValue());
      }
    }

    try {
      Long tenancyId = LoginUserInfoHolder.getCurrentLoginUserTenancyId();
      LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> configed =
          configedRequestMap.get(tenancyId);
      if (configed != null) {
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : configed.entrySet()) {
          if (entry.getKey().matches(request)) {
            allMatchedMap.put(entry.getKey(), entry.getValue());
          }
        }
      }
    } catch (UserNotLoginException ex) {
      log.warn("user not login, when call getAttributes(Object object)");
    }
    RequestMatcher requestMatcher =
        PatternMatchMost.findMachMostRequestMatcher(request, allMatchedMap);
    return requestMatcher == null ? null : allMatchedMap.get(requestMatcher);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }
}
