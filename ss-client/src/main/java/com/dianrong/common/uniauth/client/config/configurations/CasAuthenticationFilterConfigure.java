package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.UniauthCasAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.handler.JWTAuthenticationFailureHandler;
import com.dianrong.common.uniauth.client.custom.handler.SSAuthenticationFailureHandler;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class CasAuthenticationFilterConfigure implements Configure<UniauthCasAuthenticationFilter>,
    ApplicationContextAware {

  private static final String DEFAULT_FILTER_PROCESS_URL = "/login/cas";

  private ApplicationContext applicationContext;

  @Autowired
  private AuthenticationSuccessHandler ssAuthenticationSuccessHandler;

  /**
   * CAS验证失败的处理Handler.
   */
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
    String[] names = this.applicationContext
        .getBeanNamesForType(AuthenticationFailureHandler.class, false, false);
    for (String name : names) {
      Object o = this.applicationContext.getBean(name);
      if (!(o instanceof JWTAuthenticationFailureHandler)) {
        this.authenticationFailureHandler = (AuthenticationFailureHandler) o;
        break;
      }
    }

    if (authenticationFailureHandler == null) {
      SSAuthenticationFailureHandler ssAuthenticationFailureHandler =
          new SSAuthenticationFailureHandler();
      ssAuthenticationFailureHandler.setDomainDefine(domainDefine);
      ssAuthenticationFailureHandler.setAllZkNodeMap(allZkNodeMap);
      this.authenticationFailureHandler = ssAuthenticationFailureHandler;
    }
  }

  @Override
  public UniauthCasAuthenticationFilter create(Object... args) {
    UniauthCasAuthenticationFilter casAuthenticationFilter = new UniauthCasAuthenticationFilter();
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
    return UniauthCasAuthenticationFilter.class.equals(cls);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
