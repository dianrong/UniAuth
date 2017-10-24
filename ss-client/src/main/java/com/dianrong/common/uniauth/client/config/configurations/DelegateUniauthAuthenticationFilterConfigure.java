package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.DelegateUniauthAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthAbstractAuthenticationFilter;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class DelegateUniauthAuthenticationFilterConfigure implements
    Configure<DelegateUniauthAuthenticationFilter> {


  @Value("#{domainDefine.authenticationTypeList}")
  private List<AuthenticationType> authenticationTypeList;

  @Override
  public DelegateUniauthAuthenticationFilter create(Object... args) {
    List<UniauthAbstractAuthenticationFilter> authenticationFilters = new ArrayList<>(
        args.length - 1);
    for (Object arg : args) {
      if (arg instanceof UniauthAbstractAuthenticationFilter) {
        authenticationFilters.add((UniauthAbstractAuthenticationFilter) arg);
      }
    }
    DelegateUniauthAuthenticationFilter filter = new DelegateUniauthAuthenticationFilter(
        authenticationFilters, authenticationTypeList);
    filter.init();
    return filter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return DelegateUniauthAuthenticationFilter.class.equals(cls);
  }
}
