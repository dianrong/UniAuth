package com.dianrong.common.uniauth.common.customer.basicauth.cache;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by denghb on 6/22/17.
 */
@Slf4j
public class CacheMapManager {

  /**
   * 默认的过期时间为5分钟.
   */
  public static final long DEFAULT_EXPIRES_TIME_SECONDS = 5L * 60L;

  private static ConcurrentMap<String, CacheMapBo<?>> cacheMap = new ConcurrentHashMap<>(); // 缓存容器

  private static volatile CacheMapManager cacheMapManager; // 缓存实例对象

  private CacheMapManager() {}

  /**
   * 采用单例模式获取缓存对象实例.
   */
  public static CacheMapManager getInstance() {
    if (null == cacheMapManager) {
      synchronized (CacheMapManager.class) {
        if (null == cacheMapManager) {
          cacheMapManager = new CacheMapManager();
        }
      }
    }
    return cacheMapManager;
  }

  /**
   * 从缓存中获取缓存对象.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(final String key) {
    Assert.notNull(key);
    CacheMapBo<T> cacheVal = (CacheMapBo<T>) cacheMap.get(key);
    // 判断是否有这个值存在
    if (cacheVal != null) {
      if (cacheVal.isExpired()) {
        log.debug("cache {} is expired, remove from cache map!");
        delete(key);
        return null;
      }
      return cacheVal.getValue();
    }
    // no cache
    return null;
  }

  public void delete(final String key) {
    cacheMap.remove(key);
  }

  /**
   * 设置缓存.
   * 
   * @param seconds 过期的秒数.
   */
  public <T> void set(final String key, final T val, final long seconds) {
    Assert.notNull(key);
    if (seconds < 0) {
      throw new UniauthCommonException(
          "expire seconds need be a positive integer!, but not " + seconds);
    }
    CacheMapBo<T> cacheVal = new CacheMapBo<T>(val, System.currentTimeMillis() + seconds * 1000L);
    cacheMap.putIfAbsent(key, cacheVal);
    log.debug("finish refresh cache: key : {}", key);
  }

  public <T> void set(final String key, final T val) {
    set(key, val, DEFAULT_EXPIRES_TIME_SECONDS);
  }

  public void clearCache() {
    cacheMap.clear();
  }
}
