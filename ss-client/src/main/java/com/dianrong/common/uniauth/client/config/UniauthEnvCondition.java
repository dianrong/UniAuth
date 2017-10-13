package com.dianrong.common.uniauth.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.ConfigurationCondition;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 作为uniauth的所有condition父类，用于定义一个统一的 condition判断过滤.
 *
 * @author wanglin
 */
@Slf4j
public abstract class UniauthEnvCondition implements ConfigurationCondition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    try {
      // 需要存在Uniauth的特征beanDefinition才行
      beanFactory.getBeanDefinition("methodSecurityExpressionHandler");
      beanFactory.getBeanDefinition("ssAuthenticationSuccessHandler");
    } catch (NoSuchBeanDefinitionException e) {
      log.info("Current set up environment is spring boot.");
      return false;
    }
    log.debug("Current set up environment is normal spring web context.");
    return doMatchesProcess(context, metadata);
  }

  abstract boolean doMatchesProcess(ConditionContext context, AnnotatedTypeMetadata metadata);
}
