package com.dianrong.common.uniauth.client.config.configurations.redis;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.common.exp.UniauthInvalidParamterException;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class InnerRedisTemplateConfigure implements Configure<RedisTemplate> {

  @Override
  public RedisTemplate create(Object... args) {
    if (args.length != 1 || !(args[0] instanceof RedisConnectionFactory)) {
      throw new UniauthInvalidParamterException(
          "Failed to create RedisConnectionFactory, because of create parameters error!");
    }
    RedisConnectionFactory redisConnectionFactory = (RedisConnectionFactory) args[0];
    RedisSerializer<Object> redisSerializer = new JdkSerializationRedisSerializer();
    RedisTemplate redisTemplate = new StringRedisTemplate(redisConnectionFactory);
    redisTemplate.setKeySerializer(redisSerializer);
    redisTemplate.setValueSerializer(redisSerializer);
    redisTemplate.setHashKeySerializer(redisSerializer);
    redisTemplate.setHashValueSerializer(redisSerializer);
    return redisTemplate;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return RedisTemplate.class.equals(cls);
  }
}
