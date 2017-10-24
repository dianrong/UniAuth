package com.dianrong.common.uniauth.common.cache.cache;

import java.util.concurrent.TimeUnit;

import org.springframework.util.StringUtils;

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

  @Override
  public <T> T get(String key, Class<T> type) throws IllegalStateException {
    Object value = get(key);
    if (value == null) {
      return null;
    }
    if (!type.isAssignableFrom(value.getClass())) {
      throw new IllegalStateException(
          "The cache with key:" + key + " is not the type of " + type.getName());
    }
    return (T) value;
  }

  @Override
  public String getName() {
    return this.name;
  }

  public long getDefaultExpireMillis() {
    return defaultExpireMillis;
  }

  public void put(String key, Object value) {
    String decoratedKey = decorateKey(key);
    doPut(decoratedKey, value);
  }

  public void put(String key, Object value, Long expireTime, TimeUnit timeUnit) {
    String decoratedKey = decorateKey(key);
    doPut(decoratedKey, value, expireTime, timeUnit);
  }

  public Object get(String key) {
    String decoratedKey = decorateKey(key);
    return doGet(decoratedKey);
  }

  public void evict(String key) {
    String decoratedKey = decorateKey(key);
    doEvict(decoratedKey);
  }

  public abstract void doPut(String key, Object value);

  public abstract void doPut(String key, Object value, Long expireTime, TimeUnit timeUnit);

  public abstract Object doGet(String key);

  public abstract void doEvict(String key);

  /**
   * 为key添加前缀,区分不同的cache key.
   */
  protected String decorateKey(String key) {
    if (!StringUtils.hasText(key)) {
      return key;
    }
    return this.name + ":" + key;
  }

  public void setDefaultExpireMillis(long defaultExpireMillis) {
    if (defaultExpireMillis <= 0) {
      throw new UniauthCommonException("defaultExpireMillis need to be a positive integer");
    }
    this.defaultExpireMillis = defaultExpireMillis;
  }
}
