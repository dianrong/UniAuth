package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.client.exp.NoSuchConfigureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Create configure bean.
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
@Slf4j
public class ConfigureBeanCreator implements ApplicationContextAware {

  private volatile ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    log.info("ConfigureBeanCreator already set");
  }

  /**
   * Create bean with the class cls .
   * @param cls the class 
   * @return new bean 
   */
  public <T> T create(Class<T> cls) {
    Assert.notNull(cls, "cls can not be null");
    Assert.notNull(applicationContext,
        "need set applicationContext before calling create(Class<T> cls)");
    String[] names = applicationContext.getBeanNamesForType(Configure.class, true, false);
    if (names.length == 0) {
      throw new NoSuchConfigureException("no configure for class " + cls);
    }
    for (int i = 0; i < names.length; i++) {
      Configure<?> tempConfigure = (Configure<?>) applicationContext.getBean(names[i]);
      if (tempConfigure.isSupport(cls)) {
        @SuppressWarnings("unchecked")
        Configure<T> configure = (Configure<T>) tempConfigure;
        return configure.create();
      }
    }
    throw new NoSuchConfigureException("no configure for class " + cls);
  }
}
