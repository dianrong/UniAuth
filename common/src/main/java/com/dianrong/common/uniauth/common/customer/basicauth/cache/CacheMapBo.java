package com.dianrong.common.uniauth.common.customer.basicauth.cache;

import com.dianrong.common.uniauth.common.util.Assert;

/**
 * Created by denghb on 6/22/17.
 */
public class CacheMapBo<T> {
  
  private final T value;
  
  private final Long expires;

  /**
   * 构造一个会过期的缓存对象.
   */
  public CacheMapBo(T value, Long expires) {
    Assert.notNull(expires);
    this.value = value;
    this.expires = expires;
  }

  public T getValue() {
    return value;
  }

  public Long getExpires() {
    return expires;
  }
  
  /**
   * 是否已经过期.
   */
  public boolean isExpired() {
    return System.currentTimeMillis() > this.expires;
  }
}
