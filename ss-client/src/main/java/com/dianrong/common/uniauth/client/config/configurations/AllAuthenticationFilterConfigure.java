package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.AllAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthAuthenticationFilter;
import com.google.common.collect.Sets;

import java.util.Set;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class AllAuthenticationFilterConfigure implements Configure<AllAuthenticationFilter> {

  @Override
  public AllAuthenticationFilter create(Object... args) {
    AllAuthenticationFilter allAuthenticationFilter = new AllAuthenticationFilter();
    Set<UniauthAuthenticationFilter> authenticationFilters = Sets.newHashSet();
    for (Object arg : args) {
      if (arg instanceof UniauthAuthenticationFilter) {
        authenticationFilters.add((UniauthAuthenticationFilter) arg);
      }
    }
    allAuthenticationFilter.setAuthenticationFilters(authenticationFilters);
    allAuthenticationFilter.init();
    return allAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return AllAuthenticationFilter.class.equals(cls);
  }
}
