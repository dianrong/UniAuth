package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;

/**
 * Created by denghb on 6/22/17.
 */
public interface CacheService {

  Response<UserDto> getResponseUserFromCache();
  void setResponseUserToCache(Response<UserDto> response);
  UserDetailDto getUserDetailFromCache();
  void setUserDetailToCache(UserDetailDto userDetailDto);
}
