package com.dianrong.common.uniauth.server.service.cache.factory;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.service.cache.switcher.RedisCacheSwitch;
import com.dianrong.common.uniauth.server.service.cache.switcher.Switch;

import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * 在原有的JedisConnectionFactory的基础上添加开关switch, 在开关为关的情况下不尝试创建连接.
 *
 * @author wanglin
 */
public class UniauthJedisConnectionFactory extends JedisConnectionFactory {

  private Switch configSwitch;

  public UniauthJedisConnectionFactory() {}

  public UniauthJedisConnectionFactory(JedisShardInfo shardInfo) {
    super(shardInfo);
  }

  public UniauthJedisConnectionFactory(JedisPoolConfig poolConfig) {
    this((RedisSentinelConfiguration) null, poolConfig);
  }

  public UniauthJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
    this(sentinelConfig, null);
  }

  public UniauthJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig,
      JedisPoolConfig poolConfig) {
    super(sentinelConfig, poolConfig);
  }

  public UniauthJedisConnectionFactory(RedisClusterConfiguration clusterConfig) {
    super(clusterConfig);
  }

  public UniauthJedisConnectionFactory(RedisClusterConfiguration clusterConfig,
      JedisPoolConfig poolConfig) {
    super(clusterConfig, poolConfig);
  }

  @Override
  public void afterPropertiesSet() {
    if (this.configSwitch == null) {
      this.configSwitch = new RedisCacheSwitch(Boolean.TRUE.toString());
    }
    if (this.configSwitch.on()) {
      super.afterPropertiesSet();
    }
  }

  public void setConfigSwitch(Switch configSwitch) {
    Assert.notNull(configSwitch);
    this.configSwitch = configSwitch;
  }
}
