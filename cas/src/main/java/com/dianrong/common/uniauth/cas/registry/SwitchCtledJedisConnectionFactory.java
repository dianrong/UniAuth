package com.dianrong.common.uniauth.cas.registry;

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
public class SwitchCtledJedisConnectionFactory extends JedisConnectionFactory {

  /**
   * 添加开关控制参数, 默认为false.
   */
  private String connectionSwitch = Boolean.FALSE.toString();

  public SwitchCtledJedisConnectionFactory() {
  }

  public SwitchCtledJedisConnectionFactory(JedisShardInfo shardInfo) {
    super(shardInfo);
  }

  public SwitchCtledJedisConnectionFactory(JedisPoolConfig poolConfig) {
    this((RedisSentinelConfiguration) null, poolConfig);
  }

  public SwitchCtledJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig) {
    this(sentinelConfig, null);
  }

  public SwitchCtledJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig,
      JedisPoolConfig poolConfig) {
    super(sentinelConfig, poolConfig);
  }

  public SwitchCtledJedisConnectionFactory(RedisClusterConfiguration clusterConfig) {
    super(clusterConfig);
  }

  public SwitchCtledJedisConnectionFactory(RedisClusterConfiguration clusterConfig,
      JedisPoolConfig poolConfig) {
    super(clusterConfig, poolConfig);
  }

  @Override
  public void afterPropertiesSet() {
    if (Boolean.TRUE.toString().equalsIgnoreCase(this.connectionSwitch)) {
      super.afterPropertiesSet();
    }
  }

  public String getConnectionSwitch() {
    return connectionSwitch;
  }

  public void setConnectionSwitch(String connectionSwitch) {
    this.connectionSwitch = connectionSwitch;
  }
}
