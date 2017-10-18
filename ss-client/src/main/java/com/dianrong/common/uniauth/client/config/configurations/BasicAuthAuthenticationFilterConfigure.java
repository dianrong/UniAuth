package com.dianrong.common.uniauth.client.config.configurations;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.UniauthBasicAuthAuthenticationFilter;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class BasicAuthAuthenticationFilterConfigure
    implements Configure<UniauthBasicAuthAuthenticationFilter> {

  @Resource(name = "authenticationManager")
  private AuthenticationManager authenticationManager;

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Value("#{domainDefine.enableBasicAuth}")
  private boolean enable;

  @Override
  public UniauthBasicAuthAuthenticationFilter create(Object... args) {
    UniauthBasicAuthAuthenticationFilter basicAuthAuthenticationFilter =
        new UniauthBasicAuthAuthenticationFilter();
    basicAuthAuthenticationFilter.setAuthenticationManager(authenticationManager);
    basicAuthAuthenticationFilter.setEnable(enable);
    basicAuthAuthenticationFilter.setSessionAuthenticationStrategy(sas);
    return basicAuthAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return UniauthBasicAuthAuthenticationFilter.class.equals(cls);
  }
}
