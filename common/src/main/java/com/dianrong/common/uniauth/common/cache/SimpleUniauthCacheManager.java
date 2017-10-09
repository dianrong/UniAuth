package com.dianrong.common.uniauth.common.cache;

import com.dianrong.common.uniauth.common.cache.cache.MemoryCache;
import com.dianrong.common.uniauth.common.cache.cache.RedisCache;
import com.dianrong.common.uniauth.common.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;

/**
 * 根据当前Uniauth的配置情况的一个简单实现.
 */
@Slf4j public class SimpleUniauthCacheManager extends AbstractUniauthCacheManager {

  /**
   * 配置字符串.
   */
  private String configStr;

  private RedisOperations<String, Object> redisOperations;

  public SimpleUniauthCacheManager() {
  }

  public SimpleUniauthCacheManager(String configStr) {
    this.configStr = configStr;
  }

  @Override protected UniauthCache getMissingCache(String name) {
    if (useRedisCache()) {
      Assert.notNull(this.redisOperations, "user redis cache, redisOperations must set." );
      return new RedisCache(name, this.redisOperations);
    } else {
      return new MemoryCache(name);
    }
  }

  public RedisOperations<String, Object> getRedisOperations() {
    return redisOperations;
  }

  public void setRedisOperations(RedisOperations<String, Object> redisOperations) {
    Assert.notNull(redisOperations, "redisOperations can not set null");
    this.redisOperations = redisOperations;
  }

  public String getConfigStr() {
    return configStr;
  }

  public void setConfigStr(String configStr) {
    this.configStr = configStr;
  }

  private boolean useRedisCache() {
    if (Boolean.TRUE.toString().equalsIgnoreCase(this.configStr)) {
      log.debug("Current application use redis as cache implement");
      return true;
    }
    log.debug("Current application use memory as cache implement");
    return false;
  }
}
