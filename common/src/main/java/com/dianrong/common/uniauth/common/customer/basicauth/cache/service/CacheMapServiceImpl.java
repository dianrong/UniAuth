package com.dianrong.common.uniauth.common.customer.basicauth.cache.service;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.customer.basicauth.cache.CacheMapManager;

/**
 * Created by denghb on 6/22/17.
 */
public class CacheMapServiceImpl implements CacheService {

  private String USER_DETAIL_DTO_KEY = "UserDetailDto";
  private String RESPONSE_USER_KEY = "ResponseUser";

  @Override
  public Response<UserDto> getResponseUserFromCache() {
    return (Response<UserDto>) CacheMapManager.getInstance().get(RESPONSE_USER_KEY);
  }

  @Override
  public void setResponseUserToCache(Response<UserDto> response) {
    CacheMapManager.getInstance().set(RESPONSE_USER_KEY, response);
  }

  @Override
  public UserDetailDto getUserDetailFromCache() {
    return (UserDetailDto) CacheMapManager.getInstance().get(USER_DETAIL_DTO_KEY);
  }

  @Override
  public void setUserDetailToCache(UserDetailDto userDetailDto) {
    CacheMapManager.getInstance().set(USER_DETAIL_DTO_KEY, userDetailDto);
  }
}
