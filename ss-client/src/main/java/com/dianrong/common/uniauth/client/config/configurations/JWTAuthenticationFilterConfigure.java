package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.SSAuthenticationFailureHandler;
import com.dianrong.common.uniauth.client.custom.filter.UniauthJWTAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class JWTAuthenticationFilterConfigure implements Configure<UniauthJWTAuthenticationFilter> {

  @Autowired
  private UniauthJWTSecurity uniauthJWTSecurity;
  
  @Autowired
  private JWTQuery jwtQuery;
  
  @Autowired
  private AuthenticationSuccessHandler ssAuthenticationSuccessHandler;

  @Autowired(required = false)
  private AuthenticationFailureHandler authenticationFailureHandler;

  @Resource(name = "authenticationManager")
  private AuthenticationManager authenticationManager;

  @Resource(name = "sas")
  private SessionAuthenticationStrategy sas;

  @Resource(name = "uniauthConfig")
  private Map<String, String> allZkNodeMap;

  @Autowired(required = false)
  private DomainDefine domainDefine;

  @PostConstruct
  private void init() {
    if (authenticationFailureHandler == null) {
      SSAuthenticationFailureHandler ssAuthenticationFailureHandler =
          new SSAuthenticationFailureHandler();
      ssAuthenticationFailureHandler.setDomainDefine(domainDefine);
      ssAuthenticationFailureHandler.setAllZkNodeMap(allZkNodeMap);
      this.authenticationFailureHandler = ssAuthenticationFailureHandler;
    }
  }

  @Override
  public UniauthJWTAuthenticationFilter create(Object... args) {
    UniauthJWTAuthenticationFilter jwtAuthenticationFilter = new UniauthJWTAuthenticationFilter(uniauthJWTSecurity);
    jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
    jwtAuthenticationFilter.setJwtQuery(jwtQuery);
    jwtAuthenticationFilter.setAuthenticationSuccessHandler(ssAuthenticationSuccessHandler);
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
