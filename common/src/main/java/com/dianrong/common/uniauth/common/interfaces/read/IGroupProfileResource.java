package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
