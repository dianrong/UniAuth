package com.dianrong.common.uniauth.common.cache.cache;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于内存的缓存实现.
 */
@Slf4j
public class MemoryCache extends AbstractUniauthCache {

  /**
   * 不会过期的缓存时间.
   */
  private static final long NOT_EXPIRED_MILLIS = -1;

  /**
   * 刷新过期数据的时间间隔分钟数.
   */
  private static final int REFRESH_EXPIRED_CACHE_MINUTES = 10;

  private ScheduledExecutorService refreshExecutor;

  /**
   * 内存缓存的Map.
   */
  private final ConcurrentHashMap<String, ExpiredCache<?>> cacheMap;

  public MemoryCache(String name) {
    super(name);
    this.cacheMap = new ConcurrentHashMap<>(128);
    this.refreshExecutor = Executors.newSingleThreadScheduledExecutor();
    // 启动线程,定时刷新过期缓存
    this.refreshExecutor.scheduleWithFixedDelay(new Runnable() {
      @Override
      public void run() {
        refreshExpiredCache();
      }
    }, REFRESH_EXPIRED_CACHE_MINUTES, REFRESH_EXPIRED_CACHE_MINUTES, TimeUnit.MINUTES);
  }

  /**
   * 刷新过期缓存.
   */
  public synchronized void refreshExpiredCache() {
    for (Map.Entry<String, ExpiredCache<?>> entry : cacheMap.entrySet()) {
      ExpiredCache<?> cache = entry.getValue();
      String key = entry.getKey();
      // 过期或者为空,都清除掉
      if (cache == null || cache.isExpired()) {
        cacheMap.remove(key);
      }
    }
  }

  @Override
  public void doPut(String key, Object value) {
    this.cacheMap.put(key, new ExpiredCache(key));
  }

  @Override
  public void doPut(String key, Object value, Long expireTime, TimeUnit timeUnit) {
    this.cacheMap.put(key,
        new ExpiredCache(value, System.currentTimeMillis() + timeUnit.toMillis(expireTime)));
  }

  @Override
  public Object doGet(String key) {
    ExpiredCache<?> cache = this.cacheMap.get(key);
    if (cache == null) {
      return null;
    }
    // 数据已经过期
    if (cache.isExpired()) {
      this.cacheMap.remove(key);
      return null;
    }
    Object value = cache.get();
    if (value == null) {
      this.cacheMap.remove(key);
    }
    return value;
  }

  @Override
  public void doEvict(String key) {
    this.cacheMap.remove(key);
  }

  @Override
  public void clear() {
    this.cacheMap.clear();
  }

  /**
   * 会过期的缓存信息.
   */
  private static final class ExpiredCache<T> extends SoftReference<T> {

    private final Long cacheMillis;

    private ExpiredCache(T value) {
      this(value, NOT_EXPIRED_MILLIS);
    }

    private ExpiredCache(T value, Long cacheMillis) {
      super(value);
      this.cacheMillis = cacheMillis;
    }

    public boolean isExpired() {
      if (this.cacheMillis == NOT_EXPIRED_MILLIS) {
        // 永远不过期
        return false;
      }
      return this.cacheMillis < System.currentTimeMillis();
    }

    public Long getCacheMillis() {
      return cacheMillis;
    }
  }
}
