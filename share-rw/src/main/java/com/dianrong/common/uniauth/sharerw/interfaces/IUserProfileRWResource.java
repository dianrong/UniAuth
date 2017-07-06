package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserProfileResource;

import java.util.Map;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public interface IUserProfileRWResource extends IUserProfileResource {

  /**
   * 更新某个用户的扩展属性值,并且根据传入的profileId返回对应的Profile.
   */
  @PUT
  @Path("{uniauthId}")
  Response<Map<String, Object>> addOrUpdateUserProfile(@PathParam("uniauthId") Long uniauthId,
      @QueryParam("profileId") Long profileId, ProfileParam param);
}
