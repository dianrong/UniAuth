package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

/**
 * Created by denghb on 6/22/17.
 */
public interface CacheService {

  Object getDataFromCache(String key);

  void setDataToCache(Object object, String key);
}
