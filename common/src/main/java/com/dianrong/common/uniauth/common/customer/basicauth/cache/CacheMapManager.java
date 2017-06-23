package com.dianrong.common.uniauth.common.customer.basicauth.cache;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by denghb on 6/22/17.
 */
@Slf4j
public class CacheMapManager {

  public final long DEFAULT_EXPIRES_TIME_SECONDS = 5 * 60;

  private final int MAX_EXPIRES_TIME = 10 * 60 * 1000;

  private static ConcurrentMap<String, CacheMapBO> cacheMap = new ConcurrentHashMap<>(); // 缓存容器

  private volatile static CacheMapManager cacheMapManager; // 缓存实例对象

  private CacheMapManager() {

  }

  /**
   * 采用单例模式获取缓存对象实例
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

  public Object get(final String key) {
    CacheMapBO cacheMapBO = cacheMap.get(key);
    long now = new Date().getTime();
    // 判断是否有这个值存在
    if (cacheMapBO == null) {
      return null;
      // 判断保存的值是否过期
    } else if (now >= cacheMapBO.getExpires()) {
      cacheMap.remove(key);
      return null;
    } else {
      return cacheMapBO.getValue();
    }
  }

  public void delete(final String key) {
    cacheMap.remove(key);
  }

  public void set(final String key, final Object value,
      final long seconds) {
    // 增加值的工作
    CacheMapBO cacheMapBO = new CacheMapBO();
    cacheMapBO.setValue(value);
    long now = new Date().getTime();

    long expires = seconds * 1000;
    if (expires >= MAX_EXPIRES_TIME) {
      expires = MAX_EXPIRES_TIME;
    }
    expires += now;
    cacheMapBO.setExpires(expires);
    cacheMap.putIfAbsent(key, cacheMapBO);
    log.info("缓存更新完成.");
  }

  public void set(final String key, final Object value) {
    set(key, value, DEFAULT_EXPIRES_TIME_SECONDS);
  }

  public void clearCache() {
    cacheMap.clear();
  }
}
