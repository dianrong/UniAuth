package com.dianrong.common.uniauth.client.config.configurations.redis;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryDelegate;
import com.dianrong.common.uniauth.common.cache.switcher.UseRedisSwitch;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.exp.UniauthInvalidParamterException;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Configure new LogoutFilter.
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class InnerRedisConnectionFactoryConfigure implements Configure<RedisConnectionFactory> {

  @Autowired
  private UseRedisSwitch useRedisSwitch;

  @Resource(name = "ssclientInnerRedisConfiguration")
  private RedisConnectionFactoryConfiguration connectionFactoryConfiguration;

  @Override
  public RedisConnectionFactory create(Object... args) {
    if (args.length != 1 || !(args[0] instanceof JedisPoolConfig)) {
      throw new UniauthInvalidParamterException(
          "Failed to create RedisConnectionFactory, because of create parameters error!");
    }
    JedisPoolConfig jedisPoolConfig = (JedisPoolConfig) args[0];
    RedisConnectionFactoryDelegate redisConnectionFactory = new RedisConnectionFactoryDelegate(
        connectionFactoryConfiguration, jedisPoolConfig);
    redisConnectionFactory.setRedisSwitch(useRedisSwitch);
    try {
      redisConnectionFactory.afterPropertiesSet();
    } catch (Exception e) {
      throw new UniauthCommonException("Failed to create RedisConnectionFactory");
    }
    return redisConnectionFactory;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return RedisConnectionFactory.class.equals(cls);
  }
}
