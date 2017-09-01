package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * 操作组Profile相关接口.
 */
@Path("groupprofiles")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IGroupProfileResource {

  // scenario: get group profile
  @GET
  @Path("{groupId}")
  Response<Map<String, Object>> getGroupProfile(@PathParam("groupId") Integer groupId,
      @QueryParam("profileId") Long profileId, @QueryParam("time") Long time);
}
