package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.common.cache.AbstractUniauthCacheManager;
import com.dianrong.common.uniauth.common.cache.SimpleUseRedisSwitch;
import com.dianrong.common.uniauth.common.cache.UniauthCache;
import com.dianrong.common.uniauth.common.cache.cache.MemoryCache;
import com.dianrong.common.uniauth.common.cache.cache.RedisCache;
import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryConfiguration;
import com.dianrong.common.uniauth.common.cache.redis.RedisConnectionFactoryDelegate;
import com.dianrong.common.uniauth.common.customer.SwitchControl;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>可配置的UniauthCacheManager.</p> <p>1 如果外部有提供UniauthCacheManager实现,则采用其实现.</p> <p>3
 * 如果外部有提供Redis的RedisConnectionFactory实现,则采用其实现. </p> <p>如果外部没有提供上述实现,则采用默认配置.</p>
 */

@Slf4j
public class ConfigurableUniauthCacheManager extends AbstractUniauthCacheManager {

  private volatile RedisOperations<String, Object> redisOperations;

  /**
   * 统一的控制开关.
   */
  private final SwitchControl redisSwitch;

  @Resource(name = "redisDefaultConfiguration")
  private RedisConnectionFactoryConfiguration defaultConfiguration;

  @Value("#{domainDefine.innerCacheRedisConfiguration}")
  private RedisConnectionFactoryConfiguration domainDefineConfiguration;

  @Autowired(required = false)
  private RedisConnectionFactory redisConnectionFactory;

  public ConfigurableUniauthCacheManager(boolean userRedis) {
    this.redisSwitch = new SimpleUseRedisSwitch(String.valueOf(userRedis));
  }

  @Override
  protected UniauthCache getMissingCache(String name) {
    if (this.redisSwitch.isOn()) {
      insureRedisOperations();
      return new RedisCache(name, this.redisOperations);
    } else {
      return new MemoryCache(name);
    }
  }


  /**
   * 初始化redisOperations,保证Redis可用.
   */
  private void insureRedisOperations() {
    if (!this.redisSwitch.isOn()) {
      log.info("SS-Client inner cache use {}", MemoryCache.class.getName());
      return;
    }
    if (this.redisOperations == null) {
      synchronized (this) {
        if (this.redisOperations == null) {
          if (redisConnectionFactory != null) {
            log.info("Use custom RedisConnectionFactory: {}",
                this.redisConnectionFactory.getClass().getName());
            this.redisOperations = createRedisTemplate(redisConnectionFactory);
          } else {
            RedisConnectionFactory defaultRedisConnectionFactory = createRedisConnectionFactory(
                createJedisPoolConfig());
            log.info("Use default RedisConnectionFactory: {}",
                defaultRedisConnectionFactory.getClass().getName());
            this.redisOperations = createRedisTemplate(defaultRedisConnectionFactory);
          }
        }
      }
    }
  }

  /**
   * 创建一个默认的JedisPoolConfig配置参数.
   */
  protected JedisPoolConfig createJedisPoolConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(200);
    jedisPoolConfig.setTestOnBorrow(true);
    return jedisPoolConfig;
  }

  protected RedisConnectionFactory createRedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
    RedisConnectionFactoryDelegate redisConnectionFactory = new RedisConnectionFactoryDelegate(
        createRedisConnectionFactoryConfiguration(), jedisPoolConfig);
    redisConnectionFactory.setRedisSwitch(redisSwitch);
    try {
      redisConnectionFactory.afterPropertiesSet();
    } catch (Exception e) {
      throw new UniauthCommonException("Failed to create RedisConnectionFactory");
    }
    return redisConnectionFactory;
  }

  protected RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisSerializer<Object> redisSerializer = new JdkSerializationRedisSerializer();
    RedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    redisTemplate.setKeySerializer(redisSerializer);
    redisTemplate.setValueSerializer(redisSerializer);
    redisTemplate.setHashKeySerializer(redisSerializer);
    redisTemplate.setHashValueSerializer(redisSerializer);
    return redisTemplate;
  }

  protected RedisConnectionFactoryConfiguration createRedisConnectionFactoryConfiguration() {
    if (domainDefineConfiguration != null) {
      return this.domainDefineConfiguration;
    }
    return this.defaultConfiguration;
  }
}
