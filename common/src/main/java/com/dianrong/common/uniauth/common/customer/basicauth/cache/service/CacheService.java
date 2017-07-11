package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

/**
 * Created by denghb on 6/22/17.
 * 用于在Basic Auth登陆的缓存实现.
 */
public interface CacheService {

  /**
   * 根据Key从缓存中获取缓存对象
   * @param key 缓存的对象key,不能为空.
   */
  Object getDataFromCache(String key);

  /**
   * 设置缓存.
   * @param key 缓存的key.
   * @param cache 缓存的值.
   */
  void setDataToCache(String key, Object cache);
}
