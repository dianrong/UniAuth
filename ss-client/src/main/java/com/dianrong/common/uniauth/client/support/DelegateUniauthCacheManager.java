package com.dianrong.common.uniauth.client.support;

import com.dianrong.common.uniauth.client.config.ConfigureBeanCreator;
import com.dianrong.common.uniauth.common.cache.SimpleUniauthCacheManager;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

public class DelegateUniauthCacheManager extends SimpleUniauthCacheManager implements
    InitializingBean {

  @Autowired
  private ConfigureBeanCreator configureBeanCreator;

  @Override
  public void afterPropertiesSet() throws Exception {
    // 设置RedisOperations参数
    JedisPoolConfig jedisPoolConfig = configureBeanCreator.create(JedisPoolConfig.class);
    RedisConnectionFactory redisConnectionFactory = configureBeanCreator
        .create(RedisConnectionFactory.class, jedisPoolConfig);
    RedisTemplate redisTemplate = configureBeanCreator
        .create(RedisTemplate.class, redisConnectionFactory);
    super.setRedisOperations(redisTemplate);
  }
}
