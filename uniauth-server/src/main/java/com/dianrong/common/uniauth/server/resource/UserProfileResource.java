package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserProfileRWResource;

import java.util.Map;

/**
 * 用户Profile操作相关接口.
 */
public class UserProfileResource implements IUserProfileRWResource {

  @Override
  public Response<Map<String, Object>> getUserProfile(Long uniauthId, Long profileId) {
    return null;
  }

  @Override
  public Response<Map<String, Object>> getUserProfileByIdentity(String identity, Long profileId,
      Long identityType) {
    return null;
  }

  @Override
  public Response<Map<String, Object>> addOrUpdateUserProfile(Long userId, Long profileId,
      ProfileParam param) {
    return null;
  }
}
