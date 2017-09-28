package com.dianrong.common.uniauth.common.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;

import java.util.Collection;

/**
 * 管理缓存.
 */
public interface UniauthCacheManager {

  /**
   * 缓存缓存.
   * @param name 缓存的名称.
   * @return 获取到的缓存.
   */
  UniauthCache getCache(String name);



  /**
   * 获取所有的缓存的名称集合.
   */
  Collection<String> getCacheNames();


  /**
   * 当缓存对象不存在的时候生成一个缓存对象.
   * @param name 缓存对应的名称.
   */
  UniauthCache getMissingCache(String name);
}
