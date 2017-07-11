package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

import com.dianrong.common.uniauth.common.customer.basicauth.cache.CacheMapManager;

/**
 * Created by denghb on 6/22/17.
 */
public class CacheMapServiceImpl implements CacheService {

  @Override
  public Object getDataFromCache(String key) {
    return CacheMapManager.getInstance().get(key);
  }

  @Override
  public void setDataToCache(String key, Object cache) {
    CacheMapManager.getInstance().set(key, cache);
  }
}
