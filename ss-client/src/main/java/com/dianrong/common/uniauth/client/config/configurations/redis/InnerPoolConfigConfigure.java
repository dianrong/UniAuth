package com.dianrong.common.uniauth.client.config.configurations.redis;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Create JedisPoolConfig.
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public final class InnerPoolConfigConfigure implements Configure<JedisPoolConfig> {

  @Override
  public JedisPoolConfig create(Object... args) {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(200);
    jedisPoolConfig.setTestOnBorrow(true);
    return jedisPoolConfig;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return JedisPoolConfig.class.equals(cls);
  }
}
