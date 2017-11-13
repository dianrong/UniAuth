package com.dianrong.common.uniauth.common.cache;

import com.dianrong.common.uniauth.common.customer.SwitchControl;
import org.springframework.data.redis.core.RedisOperations;

import com.dianrong.common.uniauth.common.cache.cache.MemoryCache;
import com.dianrong.common.uniauth.common.cache.cache.RedisCache;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * 根据当前Uniauth的配置情况的一个简单实现.
 */
@Slf4j
public class SimpleUniauthCacheManager extends AbstractUniauthCacheManager {
  /**
   * 配置字符串.
   */
  private SwitchControl redisSwitch = new SimpleUseRedisSwitch();

  private RedisOperations<String, Object> redisOperations;

  public SimpleUniauthCacheManager() {}

  @Override
  protected UniauthCache getMissingCache(String name) {
    if (redisSwitch.isOn()) {
      Assert.notNull(this.redisOperations, "use redis cache, redisOperations must set.");
      return new RedisCache(name, this.redisOperations);
    } else {
      return new MemoryCache(name);
    }
  }

  public RedisOperations<String, Object> getRedisOperations() {
    return redisOperations;
  }

  public void setRedisOperations(RedisOperations<String, Object> redisOperations) {
    Assert.notNull(redisOperations, "redisOperations can not set null.");
    this.redisOperations = redisOperations;
  }

  public void setRedisSwitch(SwitchControl redisSwitch) {
    Assert.notNull(redisSwitch, "redisSwitch must not be null.");
    this.redisSwitch = redisSwitch;
  }
}
