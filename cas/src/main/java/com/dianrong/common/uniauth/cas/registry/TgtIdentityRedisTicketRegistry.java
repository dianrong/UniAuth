package com.dianrong.common.uniauth.cas.registry;

import com.dianrong.common.uniauth.common.util.Assert;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

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
  public void deleteTgtId(Long userId, String tgt) {
    String cacheKey = getCacheKey(userId);
    this.redisTemplate.opsForSet().remove(cacheKey, tgt);
  }

  @Override
  public Set<String> getTgtId(Long userId) {
    String cacheKey = getCacheKey(userId);
    Set<Object> result = this.redisTemplate.opsForSet().members(cacheKey);
    if (CollectionUtils.isEmpty(result)) {
      return Collections.emptySet();
    }
    Set<String> strSet = new HashSet<>(result.size());
    for (Object o : result) {
      strSet.add(o.toString());
    }
    return strSet;
  }

  @Override
  public void setTgtId(Long userId, String tgtId) {
    String cacheKey = getCacheKey(userId);
    this.redisTemplate.opsForSet().add(cacheKey, tgtId);
    // 设置过期时间
    this.redisTemplate.expire(cacheKey, tgtTimeout, TimeUnit.SECONDS);
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
