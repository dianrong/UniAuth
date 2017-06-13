package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class ConcurrentSessionFilterConfigure implements Configure<ConcurrentSessionFilter> {

  @Resource(name = "sessionRegistry")
  private SessionRegistry sessionRegistry;

  @Resource(name = "uniauthConfig")
  private Map<String, String> uniauthConfig;

  /**
   * 兼容4.2版本的Spring Security.
   */
  private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

  @Override
  public ConcurrentSessionFilter create() {
    ConcurrentSessionFilter concurrentSessionFilter = new ConcurrentSessionFilter(sessionRegistry,
        getExpiredUrl());
    concurrentSessionFilter.setRedirectStrategy(redirectStrategy);
    return concurrentSessionFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return ConcurrentSessionFilter.class.equals(cls);
  }

  private String getExpiredUrl() {
    return uniauthConfig.get("cas_server") + "/logout?dupsession=true";
  }
}
