package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.util.Assert;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于Redis的实现.
 */
@Slf4j
public class TgtIdentityRedisTicketRegistry extends AbstractTgtIdentityTicketRegistryDelegate {

  @NotNull
  private final RedisTemplate<String, Object> redisTemplate;

  /**
   * 存储身份信息与Tgt关联关系的前缀.
   */
  private String tgtIdentityPrefix = "tgtId";

  /**
   * TGT的过期时间.
   */
  @Min(0)
  private final int tgtTimeout;

  public TgtIdentityRedisTicketRegistry(RedisTicketRegistry redisTicketRegistry) {
    super(redisTicketRegistry);
    this.redisTemplate = redisTicketRegistry.getRedisTemplate();
    this.tgtTimeout = redisTicketRegistry.getTgtTimeout();
  }

  @Override
  public void deleteTgtId(Long userId) {
    this.redisTemplate.delete(getCacheKey(userId));
  }

  @Override
  public String getTgtId(Long userId) {
    return (String) this.redisTemplate.opsForValue().get(getCacheKey(userId));
  }

  @Override
  public void setTgtId(Long userId, String tgtId) {
    redisTemplate.opsForValue().set(getCacheKey(userId), tgtId, tgtTimeout, TimeUnit.SECONDS);
  }

  protected String getCacheKey(Long userId) {
    Assert.notNull(userId, "userId can not be null");
    return tgtIdentityPrefix + ":" + userId.toString();
  }

  public String getTgtIdentityPrefix() {
    return tgtIdentityPrefix;
  }

  public void setTgtIdentityPrefix(String tgtIdentityPrefix) {
    Assert.notNull(tgtIdentityPrefix, "tgtIdentityPrefix can not be null");
    this.tgtIdentityPrefix = tgtIdentityPrefix;
  }
}
