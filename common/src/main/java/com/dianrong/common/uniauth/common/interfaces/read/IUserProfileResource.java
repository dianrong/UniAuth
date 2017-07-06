package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.UserIdentityType;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 操作用户Profile相关接口.
 */
@Path("userprofiles")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserProfileResource {

  // scenario: get user profile
  @GET
  @Path("{uniauthId}?profile_id={profileId}&time={time}")
  Response<Map<String, Object>> getUserProfile(@PathParam("uniauthId") Long uniauthId,
      @PathParam("profileId") Long profileId, @PathParam("time") Long time);

  // scenario: get user profile by identity type
  @GET
  @Path("{identity}?profile_id={profileId}&tenancyId={tenancyId}&identity_type={identityType}")
  Response<Map<String, Object>> getUserProfileByIdentity(@PathParam("identity") String identity, @PathParam("tenancyId") Long tenancyId, 
      @PathParam("profileId") Long profileId, @PathParam("identityType") UserIdentityType identityType);
}
