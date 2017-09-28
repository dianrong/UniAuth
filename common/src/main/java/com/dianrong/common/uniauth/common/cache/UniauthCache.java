package com.dianrong.common.uniauth.common.cache;

import java.util.concurrent.TimeUnit;

/**
 * Uniauth内部使用的Cache接口.
 */
public interface UniauthCache {

  /**
   * 返回缓存对象的名称.
   */
  String getName();

  /**
   * 放入缓存.
   * @param key 缓存的key,不能为空.
   * @param value 缓存的值.
   */
  void put(String key, Object value);

  /**
   * 放入缓存.
   * @param key 缓存的key,不能为空.
   * @param value 缓存的值.
   * @param expireTime 过期时长.
   * @param timeUnit 时长单位
   */
  void put(String key, Object value, Long expireTime, TimeUnit timeUnit);

  /**
   * 获取缓存值.
   * @param key 缓存key.
   */
  Object get(String key);

  /**
   * 返回指定类型的缓存.
   * @param key 缓存key.
   * @param type 缓存值的类型.
   * @throws IllegalStateException 找到的缓存值与类型不匹配.
   */
  <T> T get(String key, Class<T> type) throws IllegalStateException;

  /**
   *清除指定的缓存.
   * @param key 缓存key.
   */
  void evict(String key);

  /**
   * 清空整个缓存对象.
   */
  void clear();
}
