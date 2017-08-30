package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.UserIdentityType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * 操作用户Profile相关接口.
 */
@Path("userprofiles")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserProfileResource {

  // scenario: get user profile by identity type
  @GET
  @Path("{identity}")
  Response<Map<String, Object>> getUserProfileByIdentity(@PathParam("identity") String identity,
      @QueryParam("profileId") Long profileId, @QueryParam("tenancyId") Long tenancyId,
      @QueryParam("identityType") UserIdentityType identityType, @QueryParam("time") Long time);
}
