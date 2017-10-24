package com.dianrong.common.uniauth.common.cache.redis.confactorycreator;

import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 哨兵模式的Redis的连接工厂配置.
 */
public class SentinelRedisConnectionFactoryCreator implements RedisConnectionFactoryCreator {
  @Override public RedisConnectionFactory create(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig) {
    RedisSentinelConfiguration redisSentinelConfiguration =
        new RedisSentinelConfiguration(configuration.getMaster(), configuration.getSentinels());
    JedisConnectionFactory jedisConnectionFactory =
        new JedisConnectionFactory(redisSentinelConfiguration, poolConfig);
    jedisConnectionFactory.setDatabase(configuration.getDatabase());
    jedisConnectionFactory.setPassword(configuration.getPassword());
    jedisConnectionFactory.setTimeout(configuration.getTimeout());
    return jedisConnectionFactory;
  }
}
