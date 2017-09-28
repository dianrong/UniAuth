package com.dianrong.common.uniauth.common.cache.cache;

import com.dianrong.common.uniauth.common.cache.UniauthCache;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现的缓存.
 */
public class RedisCache extends AbstractUniauthCache {

  public RedisCache(String name) {
    super(name);
  }

  @Override public String getName() {
    return null;
  }

  @Override public void put(String key, Object value) {

  }

  @Override public void put(String key, Object value, Long expireTime, TimeUnit timeUnit) {

  }

  @Override public Object get(String key) {
    return null;
  }

  @Override public <T> T get(String key, Class<T> type) throws IllegalStateException {
    return null;
  }

  @Override public void evict(String key) {

  }

  @Override public void clear() {

  }
}
