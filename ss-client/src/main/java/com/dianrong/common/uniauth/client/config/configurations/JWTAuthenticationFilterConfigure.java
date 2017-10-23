package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.util.List;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.UniauthJWTAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.handler.JWTAuthenticationFailureHandler;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class JWTAuthenticationFilterConfigure implements Configure<UniauthJWTAuthenticationFilter> {

  @Autowired
  private UniauthJWTSecurity uniauthJWTSecurity;

  @Autowired
  private JWTQuery jwtQuery;

  @Autowired
  private AuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

  @Autowired(required = false)
  private JWTAuthenticationFailureHandler authenticationFailureHandler;

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Resource(name = "authenticationManager")
  private AuthenticationManager authenticationManager;


  @Override
  public UniauthJWTAuthenticationFilter create(Object... args) {
    UniauthJWTAuthenticationFilter jwtAuthenticationFilter =
        new UniauthJWTAuthenticationFilter(uniauthJWTSecurity, jwtQuery);
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setAuthenticationSuccessHandler(jwtAuthenticationSuccessHandler);
    if (this.authenticationFailureHandler != null) {
      jwtAuthenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
    }
    jwtAuthenticationFilter.setSessionAuthenticationStrategy(sas);
    return jwtAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return UniauthJWTAuthenticationFilter.class.equals(cls);
  }
}
