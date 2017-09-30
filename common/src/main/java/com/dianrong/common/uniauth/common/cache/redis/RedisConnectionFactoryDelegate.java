package com.dianrong.common.uniauth.common.cache.redis;

import com.dianrong.common.uniauth.common.util.Assert;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 根据配置动态支持Redis的三种连接方式.
 */
public class RedisConnectionFactoryDelegate
    implements RedisConnectionFactory, InitializingBean, DisposableBean {

  /**
   * 实际连接对象.
   */
  private RedisConnectionFactory delegate;

  /**
   * 是否启用.默认是启用状态.
   */
  private Boolean on;

  public RedisConnectionFactoryDelegate(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig) {
    this(configuration, poolConfig, RedisType.SINGLE.toString());
  }

  public RedisConnectionFactoryDelegate(RedisConnectionFactoryConfiguration configuration,
      JedisPoolConfig poolConfig, String redisType) {
    Assert.notNull(configuration, "RedisConnectionFactoryConfiguration can not be null.");
    RedisType type = RedisType.toType(redisType);
    this.delegate = type.getConnectionFactoryCreator().create(configuration, poolConfig);
  }


  @Override public RedisConnection getConnection() {
    return delegate.getConnection();
  }

  @Override public RedisClusterConnection getClusterConnection() {
    return delegate.getClusterConnection();
  }

  @Override public boolean getConvertPipelineAndTxResults() {
    return delegate.getConvertPipelineAndTxResults();
  }

  @Override public RedisSentinelConnection getSentinelConnection() {
    return delegate.getSentinelConnection();
  }

  @Override public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
    return delegate.translateExceptionIfPossible(ex);
  }

  @Override public void destroy() throws Exception {
    if (getOn()) {
      if (delegate instanceof DisposableBean) {
        ((DisposableBean) delegate).destroy();
      }
    }
  }

  @Override public void afterPropertiesSet() throws Exception {
    if (getOn()) {
      if (delegate instanceof InitializingBean) {
        ((InitializingBean) delegate).afterPropertiesSet();
      }
    }
  }

  /**
   * 是否处于启用状态.
   */
  public Boolean getOn() {
    if (on == null) {
      // 默认是True
      return true;
    }
    return on;
  }

  public void setOn(Boolean on) {
    this.on = on;
  }
}
