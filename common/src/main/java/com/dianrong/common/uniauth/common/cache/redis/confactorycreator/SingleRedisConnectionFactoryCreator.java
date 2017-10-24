package com.dianrong.common.uniauth.common.cache.redis.confactorycreator;

import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 单节点Redis的连接工厂配置.
 */
public class SingleRedisConnectionFactoryCreator implements RedisConnectionFactoryCreator {
  @Override
  public RedisConnectionFactory create(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig) {
    JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
    jedisConnectionFactory.setHostName(configuration.getHost());
    jedisConnectionFactory.setPort(configuration.getPort());
    jedisConnectionFactory.setDatabase(configuration.getDatabase());
    jedisConnectionFactory.setPassword(configuration.getPassword());
    jedisConnectionFactory.setTimeout(configuration.getTimeout());
    return jedisConnectionFactory;
  }
}
