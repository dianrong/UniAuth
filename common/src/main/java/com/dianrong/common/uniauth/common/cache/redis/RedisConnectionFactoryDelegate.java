package com.dianrong.common.uniauth.common.cache.redis;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;

import com.dianrong.common.uniauth.common.cache.switcher.SimpleUseRedisSwitch;
import com.dianrong.common.uniauth.common.cache.switcher.UseRedisSwitch;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 根据配置动态支持Redis的三种连接方式.
 */
@Slf4j
public class RedisConnectionFactoryDelegate
    implements RedisConnectionFactory, InitializingBean, DisposableBean {

  /**
   * 实际连接对象.
   */
  private RedisConnectionFactory delegate;

  /**
   * 配置字符串.
   */
  private UseRedisSwitch redisSwitch = new SimpleUseRedisSwitch();

  public RedisConnectionFactoryDelegate(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig) {
    Assert.notNull(configuration, "RedisConnectionFactoryConfiguration can not be null.");
    RedisType type = configuration.getType();
    log.info("Current application use redis connection type is:" + type.toString());
    this.delegate = type.getConnectionFactoryCreator().create(configuration, poolConfig);
  }

  @Override
  public RedisConnection getConnection() {
    return delegate.getConnection();
  }

  @Override
  public RedisClusterConnection getClusterConnection() {
    return delegate.getClusterConnection();
  }

  @Override
  public boolean getConvertPipelineAndTxResults() {
    return delegate.getConvertPipelineAndTxResults();
  }

  @Override
  public RedisSentinelConnection getSentinelConnection() {
    return delegate.getSentinelConnection();
  }

  @Override
  public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
    return delegate.translateExceptionIfPossible(ex);
  }

  @Override
  public void destroy() throws Exception {
    if (this.redisSwitch.isOn()) {
      if (delegate instanceof DisposableBean) {
        ((DisposableBean) delegate).destroy();
      }
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    if (this.redisSwitch.isOn()) {
      if (delegate instanceof InitializingBean) {
        ((InitializingBean) delegate).afterPropertiesSet();
      }
    }
  }

  public void setRedisSwitch(UseRedisSwitch redisSwitch) {
    Assert.notNull(redisSwitch, "redisSwitch must not be null.");
    this.redisSwitch = redisSwitch;
  }
}
