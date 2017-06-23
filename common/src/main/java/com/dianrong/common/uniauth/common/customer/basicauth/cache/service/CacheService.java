package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

import com.dianrong.common.uniauth.common.customer.basicauth.cache.CacheMapBO;

/**
 * Created by denghb on 6/22/17.
 */
public interface CacheService {

  Object getDataFromCache(String key);

  void setDataToCache(CacheMapBO cacheMapBO, String key);
}
