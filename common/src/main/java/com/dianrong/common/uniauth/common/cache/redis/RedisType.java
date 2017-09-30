package com.dianrong.common.uniauth.common.cache.redis;

import com.dianrong.common.uniauth.common.cache.redis.confactorycreator.ClusterRedisConnectionFactoryCreator;
import com.dianrong.common.uniauth.common.cache.redis.confactorycreator.RedisConnectionFactoryCreator;
import com.dianrong.common.uniauth.common.cache.redis.confactorycreator.SentinelRedisConnectionFactoryCreator;
import com.dianrong.common.uniauth.common.cache.redis.confactorycreator.SingleRedisConnectionFactoryCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * Redis的类型配置.
 */

@Slf4j public enum RedisType {
  /**
   * 部署单节点的模式.
   */
  SINGLE(new SingleRedisConnectionFactoryCreator()),

  /**
   * 集群模式.
   */
  CLUSTER(new ClusterRedisConnectionFactoryCreator()),

  /**
   * 哨兵模式.
   */
  SENTINEL(new SentinelRedisConnectionFactoryCreator());


  private final RedisConnectionFactoryCreator connectionFactoryCreator;

  private RedisType(RedisConnectionFactoryCreator connectionFactoryCreator) {
    this.connectionFactoryCreator = connectionFactoryCreator;
  }

  public RedisConnectionFactoryCreator getConnectionFactoryCreator() {
    return connectionFactoryCreator;
  }

  public static RedisType toType(String type) {
    if (!StringUtils.hasText(type)) {
      log.warn("RedisType string is empty, so just return default RedisType:SINGLE");
      return SINGLE;
    }
    for (RedisType rt : RedisType.values()) {
      if (rt.toString().equalsIgnoreCase(type.trim())) {
        return rt;
      }
    }
    log.warn("{} is a invalid RedisType String, so just return default RedisType:SINGLE", type);
    return SINGLE;
  }
}
