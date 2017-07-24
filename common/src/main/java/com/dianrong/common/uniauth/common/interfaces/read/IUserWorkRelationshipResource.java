package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.request.UserWorkRelationshipParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * UserWorkRelationship的读接口定义.
 */
@Path("user-work-relationship")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserWorkRelationshipResource {

  @POST
  @Path("search-by-user-id")
  // scenario: search user_detail
  Response<UserWorkRelationshipDto> searchByUserId(UserWorkRelationshipParam param);
}
