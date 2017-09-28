package com.dianrong.common.uniauth.common.cache.cache;

import com.dianrong.common.uniauth.common.cache.UniauthCache;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * UniauthCache的模板类.
 */
public abstract class AbstractUniauthCache implements UniauthCache {

  /**
   * 不会过期的缓存时间.
   */
  private static final long NOT_EXPIRED_MILLIS = -1;

  /**
   * 默认的缓存过期的毫秒数.
   */
  private long defaultExpireMillis = NOT_EXPIRED_MILLIS;

  private final String name;

  public AbstractUniauthCache(String name) {
    Assert.notNull(name, "Cache name must not be null");
    this.name = name;
  }

  @Override public String getName() {
    return this.name;
  }

  public long getDefaultExpireMillis() {
    return defaultExpireMillis;
  }

  public void setDefaultExpireMillis(long defaultExpireMillis) {
    if (defaultExpireMillis <= 0) {
      throw new UniauthCommonException("defaultExpireMillis need to be a positive integer");
    }
    this.defaultExpireMillis = defaultExpireMillis;
  }
}
