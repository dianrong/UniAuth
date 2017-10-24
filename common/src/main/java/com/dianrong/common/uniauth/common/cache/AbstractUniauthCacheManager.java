package com.dianrong.common.uniauth.common.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractUniauthCacheManager implements UniauthCacheManager{

  private ConcurrentHashMap<String, UniauthCache> cachesMap;

  public AbstractUniauthCacheManager() {
    this.cachesMap = new ConcurrentHashMap<>(8);
  }

  public UniauthCache getCache(String name) {
    UniauthCache cache = this.cachesMap.get(name);
    if (cache == null) {
      cache = getMissingCache(name);
      this.cachesMap.putIfAbsent(name, cache);
    }
    return this.cachesMap.get(name);
  }


  public Collection<String> getCacheNames(){
    Set<String> cacheNames = this.cachesMap.keySet();
    return Collections.unmodifiableSet(cacheNames);
  }


  /**
   * 当缓存对象不存在的时候生成一个缓存对象.
   * @param name 缓存对应的名称.
   * @return 不能返回空.
   */
  protected abstract UniauthCache getMissingCache(String name);
}
