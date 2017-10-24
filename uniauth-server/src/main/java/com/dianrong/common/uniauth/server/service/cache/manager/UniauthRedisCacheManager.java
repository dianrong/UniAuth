package com.dianrong.common.uniauth.server.service.cache.manager;

import com.dianrong.common.uniauth.common.cache.switcher.SimpleUseRedisSwitch;
import com.dianrong.common.uniauth.common.cache.switcher.UseRedisSwitch;
import com.dianrong.common.uniauth.common.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Collection;

@Slf4j
public class UniauthRedisCacheManager implements CacheManager, InitializingBean {

  /**
   * 配置字符串.
   */
  private UseRedisSwitch redisSwitch;

  private CacheManager cacheManager;

  private RedisCacheManager redisCacheManager;

  /**
   * 主要是为了配置默认的RedisCacheManager.
   */
  private boolean allowNullValues = true;

  private int expireSeconds;

  public void setRedisSwitch(UseRedisSwitch redisSwitch) {
    Assert.notNull(redisSwitch, "redisSwitch must not be null.");
    this.redisSwitch = redisSwitch;
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
    if (this.redisSwitch == null) {
      this.redisSwitch = new SimpleUseRedisSwitch(Boolean.TRUE.toString());
    }
    if (this.redisSwitch.isOn()) {
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
