package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.UniauthBasicAuthAuthenticationFilter;
import javax.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class BasicAuthAuthenticationFilterConfigure
    implements Configure<UniauthBasicAuthAuthenticationFilter> {

  @Resource(name = "authenticationManager")
  private AuthenticationManager authenticationManager;

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Override
  public UniauthBasicAuthAuthenticationFilter create(Object... args) {
    UniauthBasicAuthAuthenticationFilter basicAuthAuthenticationFilter =
        new UniauthBasicAuthAuthenticationFilter();
    basicAuthAuthenticationFilter.setAuthenticationManager(authenticationManager);
    basicAuthAuthenticationFilter.setSessionAuthenticationStrategy(sas);
    return basicAuthAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return UniauthBasicAuthAuthenticationFilter.class.equals(cls);
  }
}
