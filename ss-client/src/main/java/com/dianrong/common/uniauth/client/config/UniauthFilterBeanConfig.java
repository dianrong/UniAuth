package com.dianrong.common.uniauth.client.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置uniauth中的filter类型的bean.
 *
 * @author wanglin
 */
@Configuration
@Conditional(UniauthFilterBeanCreateCondition.class)
public class UniauthFilterBeanConfig {

  @Autowired
  private ConfigureBeanCreator configureBeanCreator;

  @Bean(name = "singleLogoutFilter")
  public SingleSignOutFilter getSingleLogoutFilter() {
    return configureBeanCreator.create(SingleSignOutFilter.class);
  }

  @Bean(name = "requestSingleLogoutFilter")
  public LogoutFilter getLogoutFilter() {
    return configureBeanCreator.create(LogoutFilter.class);
  }

  // Redis
  @Bean(name = "ssclientInnerPoolConfig")
  public JedisPoolConfig getJedisPoolConfig() {
    return configureBeanCreator.create(JedisPoolConfig.class);
  }

  @Bean(name = "ssclientInnerRedisConnectionFactory")
  public RedisConnectionFactory getRedisConnectionFactory() {
    return configureBeanCreator.create(RedisConnectionFactory.class, getJedisPoolConfig());
  }

  @Bean(name = "ssclientInnerRedisTemplate")
  public RedisTemplate getRedisTemplate() {
    return configureBeanCreator.create(RedisTemplate.class, getRedisConnectionFactory());
  }
}
