package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import javax.annotation.Resource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DelegateUniauthCacheManager implements FactoryBean<UniauthCacheManager>,
    ApplicationContextAware {

  private static final String DEFAULT_CACHE_MANANGER_NAME = "defaultUniauthCacheManager";

  private static final String CACHE_MANANGER_NAME = "uniauthCacheManager";

  private ApplicationContext applicationContext;

  @Resource(name = "defaultUniauthCacheManager")
  private UniauthCacheManager defaultUniauthCacheManager;

  @Override
  public UniauthCacheManager getObject() throws Exception {
    UniauthCacheManager customUniauthCacheManager = getCustomUniauthCacheManager();
    if (customUniauthCacheManager != null) {
      return customUniauthCacheManager;
    }
    return defaultUniauthCacheManager;
  }

  @Override
  public Class<?> getObjectType() {
    return UniauthCacheManager.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  /**
   * 获取用户自定义的UniauthCacheManager实例.
   *
   * @return 如果吗没有自定义, 则返回null.
   */
  private UniauthCacheManager getCustomUniauthCacheManager() {
    String[] uniauthCacheManagerNames = this.applicationContext
        .getBeanNamesForType(UniauthCacheManager.class);
    for (String name : uniauthCacheManagerNames) {
      if (DEFAULT_CACHE_MANANGER_NAME.equals(name) || CACHE_MANANGER_NAME.equals(name)) {
        continue;
      }
      return this.applicationContext.getBean(name, UniauthCacheManager.class);
    }
    return null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
