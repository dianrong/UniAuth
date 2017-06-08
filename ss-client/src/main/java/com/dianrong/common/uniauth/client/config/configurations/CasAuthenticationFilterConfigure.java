package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.SSAuthenticationFailureHandler;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class CasAuthenticationFilterConfigure implements Configure<CasAuthenticationFilter> {

  private static final String DEFAULT_FILTER_PROCESS_URL = "/login/cas";

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
  public CasAuthenticationFilter create() {
    CasAuthenticationFilter casAuthenticationFilter = new CasAuthenticationFilter();
    casAuthenticationFilter.setAuthenticationManager(authenticationManager);
    casAuthenticationFilter.setFilterProcessesUrl(DEFAULT_FILTER_PROCESS_URL);
    casAuthenticationFilter.setAuthenticationSuccessHandler(ssAuthenticationSuccessHandler);
    if (this.authenticationFailureHandler != null) {
      casAuthenticationFilter.setAuthenticationFailureHandler(this.authenticationFailureHandler);
    }
    casAuthenticationFilter.setSessionAuthenticationStrategy(sas);
    return casAuthenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return CasAuthenticationFilter.class.equals(cls);
  }
}
