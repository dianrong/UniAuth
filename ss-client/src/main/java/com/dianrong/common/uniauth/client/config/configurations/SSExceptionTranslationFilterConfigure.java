package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.CustomizedRedirectFormat;
import com.dianrong.common.uniauth.client.custom.SSExceptionTranslationFilter;
import com.dianrong.common.uniauth.client.custom.UniauthAjaxResponseProcessor;
import com.dianrong.common.uniauth.client.custom.UniauthSimpleAccessDeniedHandler;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class SSExceptionTranslationFilterConfigure
    implements Configure<SSExceptionTranslationFilter> {

  @Autowired(required = false)
  private AccessDeniedHandler accessDeniedHandlerImpl;

  @Autowired
  private AuthenticationEntryPoint casAuthEntryPoint;

  @Autowired
  private ZooKeeperConfig zooKeeperConfig;

  @Autowired(required = false)
  private CustomizedRedirectFormat customizedRedirectFormat;

  @Override
  public SSExceptionTranslationFilter create() {
    if (accessDeniedHandlerImpl == null) {
      log.debug("Do not special AccessDeniedHandler, so use default AccessDeniedHandler: {}",
          UniauthSimpleAccessDeniedHandler.class.getName());
      this.accessDeniedHandlerImpl = new UniauthSimpleAccessDeniedHandler();
    }

    SSExceptionTranslationFilter ssExceptionTranslationFilter =
        new SSExceptionTranslationFilter(casAuthEntryPoint);
    ssExceptionTranslationFilter.setAccessDeniedHandler(accessDeniedHandlerImpl);
    ssExceptionTranslationFilter.setZooKeeperConfig(zooKeeperConfig);
    ssExceptionTranslationFilter.setCustomizedRedirectFormat(customizedRedirectFormat);
    ssExceptionTranslationFilter
        .setAjaxResponseProcessor(new UniauthAjaxResponseProcessor(this.zooKeeperConfig));
    return ssExceptionTranslationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return SSExceptionTranslationFilter.class.equals(cls);
  }
}
