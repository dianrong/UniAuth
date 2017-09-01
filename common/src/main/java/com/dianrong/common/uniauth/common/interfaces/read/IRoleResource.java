package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("role")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IRoleResource {

  @GET
  @Path("allrolecodes")
  // scenario: get all role codes, dictionary data from role_code table
  Response<List<RoleCodeDto>> getAllRoleCodes();

  @POST
  @Path("searchroles")
  // scenario: search role
  Response<PageDto<RoleDto>> searchRole(RoleQuery roleQuery);

  @POST
  @Path("domain/perms")
  // scenario: retrieve all permissions connected with a role(including all other permissions
  // under a domain)
  Response<List<PermissionDto>> getAllPermsToRole(RoleParam roleParam);
}
