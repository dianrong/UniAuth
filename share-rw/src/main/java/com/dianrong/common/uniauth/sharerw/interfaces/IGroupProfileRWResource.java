package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupProfileResource;

import java.util.Map;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

public interface IGroupProfileRWResource extends IGroupProfileResource {

  // scenario: add or update group profile
  @PUT
  @Path("{groupId}")
  Response<Map<String, Object>> addOrUpdateGrpProfile(@PathParam("groupId") Integer groupId,
      @QueryParam("profileId") Long profileId, ProfileParam param);
}
