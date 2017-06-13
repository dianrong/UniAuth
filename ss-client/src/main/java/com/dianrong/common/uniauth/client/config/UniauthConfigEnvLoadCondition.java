package com.dianrong.common.uniauth.client.config;

import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Decide whether uniauth's config env is loaded.
 *
 * @author wanglin
 */
public class UniauthConfigEnvLoadCondition extends UniauthEnvCondition {

  @Override
  public ConfigurationPhase getConfigurationPhase() {
    return ConfigurationPhase.REGISTER_BEAN;
  }

  @Override
  boolean doMatchesProcess(ConditionContext context, AnnotatedTypeMetadata metadata) {
    // UniauthEnvCondition 通过了，则代表uniauth的config环境加载了
    return true;
  }
}
