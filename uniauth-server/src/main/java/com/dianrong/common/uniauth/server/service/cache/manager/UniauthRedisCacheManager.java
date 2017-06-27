package com.dianrong.common.uniauth.server.service.cache.manager;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.service.cache.switcher.RedisCacheSwitch;
import com.dianrong.common.uniauth.server.service.cache.switcher.Switch;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

@Slf4j
public class UniauthRedisCacheManager implements CacheManager, InitializingBean {

  private Switch configSwitch;

  private CacheManager cacheManager;

  private RedisCacheManager redisCacheManager;

  /**
   * 主要是为了配置默认的RedisCacheManager.
   */
  private boolean allowNullValues = true;

  private int expireSeconds;

  public void setConfigSwitch(Switch configSwitch) {
    Assert.notNull(configSwitch);
    this.configSwitch = configSwitch;
  }

  @Override
  public Cache getCache(String name) {
    return this.cacheManager.getCache(name);
  }

  @Override
  public Collection<String> getCacheNames() {
    return this.cacheManager.getCacheNames();
  }

  public int getExpireSeconds() {
    return expireSeconds;
  }

  public void setExpireSeconds(int expireSeconds) {
    this.expireSeconds = expireSeconds;
  }

  /**
   * Bean的初始化函数.
   */
  public void init() throws Exception {
    afterPropertiesSet();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.configSwitch == null) {
      this.configSwitch = new RedisCacheSwitch(Boolean.TRUE.toString());
    }
    if (this.configSwitch.on()) {
      Assert.notNull(this.redisCacheManager,
          "use useRedisCacheManager, useRedisCacheManager can not be null");
      log.info("Spring Cache use RedisCacheManager");
      this.redisCacheManager.setDefaultExpiration(expireSeconds);
      this.cacheManager = this.redisCacheManager;
    } else {
      log.info("Spring Cache use default Simple CacheManager");
      this.cacheManager = new DynamicConfigCacheCacheManager(expireSeconds, this.allowNullValues);
    }
  }

  public void setRedisCacheManager(RedisCacheManager redisCacheManager) {
    this.redisCacheManager = redisCacheManager;
  }

  public boolean isAllowNullValues() {
    return allowNullValues;
  }

  public void setAllowNullValues(boolean allowNullValues) {
    this.allowNullValues = allowNullValues;
  }
}
