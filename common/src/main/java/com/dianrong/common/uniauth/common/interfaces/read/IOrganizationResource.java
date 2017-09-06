package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.GroupQuery;
import com.dianrong.common.uniauth.common.bean.request.OrganizationParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 组织机构的读接口定义.
 */
@Path("organization")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IOrganizationResource {

  @POST
  @Path("tree")
  Response<OrganizationDto> getOrganizationTree(OrganizationParam organizationParam);

  @POST
  @Path("owners")
  Response<List<UserDto>> getOrganizationOwners(PrimaryKeyParam primaryKeyParam);

  @POST
  @Path("check-owner")
  Response<Void> checkOwner(OrganizationParam organizationParam);
}
