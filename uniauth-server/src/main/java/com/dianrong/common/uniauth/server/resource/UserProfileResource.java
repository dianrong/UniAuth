package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.server.service.UserProfileService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserProfileRWResource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户Profile操作相关接口.
 */
public class UserProfileResource implements IUserProfileRWResource {

  @Autowired
  private UserProfileService userProfileService;

  @Override
  public Response<Map<String, Object>> getUserProfile(Long uniauthId, Long profileId) {
    return Response.success(userProfileService.getUserProfile(uniauthId, profileId));
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
