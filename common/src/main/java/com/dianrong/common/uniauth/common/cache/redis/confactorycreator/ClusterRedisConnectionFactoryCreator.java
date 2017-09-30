package com.dianrong.common.uniauth.common.cache.redis.confactorycreator;

import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 集群模式的Redis连接工厂配置.
 */
public class ClusterRedisConnectionFactoryCreator implements RedisConnectionFactoryCreator {
  @Override public RedisConnectionFactory create(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig) {
    RedisClusterConfiguration redisClusterConfiguration =
        new RedisClusterConfiguration(configuration.getClusterNodes());
    redisClusterConfiguration.setMaxRedirects(configuration.getMaxRedirects());
    JedisConnectionFactory jedisConnectionFactory =
        new JedisConnectionFactory(redisClusterConfiguration, poolConfig);
    jedisConnectionFactory.setDatabase(configuration.getDatabase());
    jedisConnectionFactory.setPassword(configuration.getPassword());
    jedisConnectionFactory.setTimeout(configuration.getTimeout());
    return jedisConnectionFactory;
  }
}
