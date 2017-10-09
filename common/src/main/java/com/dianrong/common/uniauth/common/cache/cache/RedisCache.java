package com.dianrong.common.uniauth.common.cache.cache;

import com.dianrong.common.uniauth.common.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现的缓存.
 */
@Slf4j
public class RedisCache extends AbstractUniauthCache {

  private final RedisOperations<String, Object> redisOperations;

  public RedisCache(String name, RedisOperations<String, Object> redisOperations) {
    super(name);
    Assert.notNull(redisOperations, "redisOperations must not be null");
    this.redisOperations = redisOperations;
  }

  @Override public void put(String key, Object value) {
    this.redisOperations.opsForValue().set(key, value);
  }

  @Override public void put(String key, Object value, Long expireTime, TimeUnit timeUnit) {
    this.redisOperations.opsForValue().set(key, value, expireTime, timeUnit);
  }

  @Override public Object get(String key) {
    return this.redisOperations.opsForValue().get(key);
  }

  @Override public void evict(String key) {
    this.redisOperations.delete(key);
  }

  @Override public void clear() {
    log.debug("RedisCache do not support clear()");
  }
}
