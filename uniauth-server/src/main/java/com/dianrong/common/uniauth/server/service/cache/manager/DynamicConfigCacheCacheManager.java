package com.dianrong.common.uniauth.server.service.cache.manager;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.util.Asserts;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;

/**
 * 自动配置Cache的简单CacheManager.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class DynamicConfigCacheCacheManager extends SimpleCacheManager {
  /**
   * 是否允许缓存值为空.
   */
  private boolean allowNullValues = true;

  /**
   * 缓存刷新的时间间隔秒数.
   */
  private int expireSeconds;

  /**
   * 定时清空缓存的线程池.
   */
  private ScheduledExecutorService excutor;

  public DynamicConfigCacheCacheManager() {
    this(0);
  }

  public DynamicConfigCacheCacheManager(int expireSeconds) {
    this(expireSeconds, true);
  }

  /**
   * 构造函数.
   * @param expireSeconds 过期的秒数.
   * @param allowNullValues 是否允许空值.
   */
  public DynamicConfigCacheCacheManager(int expireSeconds, boolean allowNullValues) {
    this.setExpireSeconds(expireSeconds);
    this.setAllowNullValues(allowNullValues);
    this.initCacheExpireProcess();
  }

  private void initCacheExpireProcess() {
    if (this.expireSeconds == 0) {
      log.info("expireSeconds is 0, so do not processing cache!");
    }
    log.info("start processing cache per {} seconds", this.expireSeconds);
    this.excutor = Executors.newSingleThreadScheduledExecutor();
    // 开启定时刷新缓存任务.
    this.excutor.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        clearCache();
      }
    }, this.expireSeconds, this.expireSeconds, TimeUnit.SECONDS);
  }

  /**
   * 处理缓存.
   */
  private synchronized void clearCache() {
    Collection<String> cacheNames = super.getCacheNames();
    for (String cacheName : cacheNames) {
      Cache cache = super.lookupCache(cacheName);
      if (cache != null) {
        cache.clear();
      }
    }
  }

  /**
   * 当Cache不存在的时候生成一个Cache.
   */
  @Override
  protected Cache getMissingCache(String name) {
    log.debug("create a new ConcurrentMapCache[{}]", name);
    return new ConcurrentMapCache(name, this.allowNullValues);
  }


  public boolean isAllowNullValues() {
    return allowNullValues;
  }

  private void setAllowNullValues(boolean allowNullValues) {
    log.debug("set new allowNullValues {}", allowNullValues);
    this.allowNullValues = allowNullValues;
  }

  public int getExpireSeconds() {
    return expireSeconds;
  }

  private void setExpireSeconds(int expireSeconds) {
    Asserts.check(expireSeconds >= 0,
        "expire seconds need a positive integer, not " + expireSeconds);
    log.debug("set expire seconds {}", expireSeconds);
    this.expireSeconds = expireSeconds;
  }
}
