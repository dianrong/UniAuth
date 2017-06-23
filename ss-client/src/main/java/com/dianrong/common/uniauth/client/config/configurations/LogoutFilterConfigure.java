package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.CompatibleAjaxLoginSuccessHandler;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * Configure new LogoutFilter.
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class LogoutFilterConfigure implements Configure<LogoutFilter> {

  private static final String DEFAULT_FILTER_PROCESS_URL = "/logout/cas";

  @Resource(name = "uniauthConfig")
  private Map<String, String> uniauthConfig;

  @Resource(name = "securityContextLogoutHandler")
  private LogoutHandler securityContextLogoutHandler;
  
  @Autowired(required = false)
  private LogoutSuccessHandler logoutSuccessHandler;

  @Override
  public LogoutFilter create() {
    if (logoutSuccessHandler == null) {
      CompatibleAjaxLoginSuccessHandler logoutSuccessHandler = new CompatibleAjaxLoginSuccessHandler();
      logoutSuccessHandler.setDefaultTargetUrl(getCasLogoutUrl());
      this.logoutSuccessHandler = logoutSuccessHandler;
    } 
    LogoutFilter logoutFilter = new LogoutFilter(this.logoutSuccessHandler, securityContextLogoutHandler);
    logoutFilter.setFilterProcessesUrl(DEFAULT_FILTER_PROCESS_URL);
    return logoutFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return LogoutFilter.class.equals(cls);
  }

  private String getCasLogoutUrl() {
    return uniauthConfig.get("cas_server") + "/logout";
  }
}
