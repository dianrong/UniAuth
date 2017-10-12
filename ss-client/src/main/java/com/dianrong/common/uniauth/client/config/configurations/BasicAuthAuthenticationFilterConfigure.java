package com.dianrong.common.uniauth.client.config.configurations;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.UniauthBasicAuthAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.handler.JWTAuthenticationFailureHandler;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class BasicAuthAuthenticationFilterConfigure
    implements Configure<UniauthBasicAuthAuthenticationFilter> {

  @Autowired
  private AuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

  @Autowired
  private JWTAuthenticationFailureHandler authenticationFailureHandler;

  @Resource(name = "authenticationManager")
  private AuthenticationManager authenticationManager;

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Autowired
  private UniauthCacheManager uniauthCacheManager;

  @Override
  public UniauthBasicAuthAuthenticationFilter create(Object... args) {
    UniauthBasicAuthAuthenticationFilter basicAuthAuthenticationFilter =
        new UniauthBasicAuthAuthenticationFilter(uniauthCacheManager);
    basicAuthAuthenticationFilter.setAuthenticationManager(authenticationManager);
    basicAuthAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
    if (this.authenticationFailureHandler != null) {
      basicAuthAuthenticationFilter
          .setAuthenticationFailureHandler(this.authenticationFailureHandler);
    }
    basicAuthAuthenticationFilter.setSessionAuthenticationStrategy(sas);
    return basicAuthAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return UniauthBasicAuthAuthenticationFilter.class.equals(cls);
  }
}
