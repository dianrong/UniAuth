package com.dianrong.common.uniauth.common.cache.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisOperations;

import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

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

  @Override
  public void doPut(String key, Object value) {
    this.redisOperations.opsForValue().set(key, value);
  }

  @Override
  public void doPut(String key, Object value, Long expireTime, TimeUnit timeUnit) {
    this.redisOperations.opsForValue().set(key, value, expireTime, timeUnit);
  }

  @Override
  public Object doGet(String key) {
    return this.redisOperations.opsForValue().get(key);
  }

  @Override
  public void doEvict(String key) {
    this.redisOperations.delete(key);
  }

  @Override
  public void clear() {
    log.debug("RedisCache do not support clear()");
  }
}
