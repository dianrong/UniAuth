package com.dianrong.common.uniauth.client.config;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * Decide whether create filter type bean auto.
 *
 * @author wanglin
 */
public class UniauthFilterBeanCreateCondition extends UniauthEnvCondition {

  @Override
  public ConfigurationPhase getConfigurationPhase() {
    return ConfigurationPhase.REGISTER_BEAN;
  }

  @Override
  boolean doMatchesProcess(ConditionContext context, AnnotatedTypeMetadata metadata) {
    ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    // 使用比较笨的方式来增加一种判断机制
    String[] names = BeanFactoryUtils
        .beanNamesForTypeIncludingAncestors(beanFactory, UniauthFilterBeanCreateAutoSign.class,
            true, false);
    if (names.length > 0) {
      return true;
    }
    // 自动判断
    names = BeanFactoryUtils
        .beanNamesForTypeIncludingAncestors(beanFactory, UniauthSecurityConfig.class, true, false);
    if (names.length > 0) {
      return false;
    }
    return true;
  }
}
