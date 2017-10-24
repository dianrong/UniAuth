package com.dianrong.common.uniauth.common.cache.redis.confactorycreator;

import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 创建Redis的连接工厂.
 */
public interface RedisConnectionFactoryCreator {
  RedisConnectionFactory create(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig);
}
