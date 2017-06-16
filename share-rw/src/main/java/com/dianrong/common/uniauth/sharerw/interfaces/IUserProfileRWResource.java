package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserProfileResource;

import java.util.Map;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface IUserProfileRWResource extends IUserProfileResource {

  // scenario: add or update user profile
  @PUT
  @Path("{userId}?profile_id={profileId}")
  Response<Map<String, Object>> addOrUpdateUserProfile(@PathParam("userId") Long userId,
      @PathParam("profileId") Long profileId, ProfileParam param);
}
