package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.OrganizationDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.OrganizationParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;
import com.dianrong.common.uniauth.common.interfaces.read.IOrganizationResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 组织关系的写接口定义.
 */
public interface IOrganizationRWResource extends IOrganizationResource {
  
  @POST
  @Path("add-users")
  Response<Void> addUsersIntoOrganization(UserListParam userListParam);

  @POST
  @Path("remove-users")
  Response<Void> removeUsersFromOrganization(UserListParam userListParam);

  @POST
  @Path("move-users")
  Response<Void> moveOrganizationUser(UserListParam userListParam);

  @POST
  @Path("add-organization")
  Response<OrganizationDto> addNewOrganizationIntoOrganization(OrganizationParam organizationParam);

  @POST
  @Path("update-organization")
  Response<OrganizationDto> updateOrganization(OrganizationParam organizationParam);

  @POST
  @Path("delete-organization")
  Response<OrganizationDto> deleteOrganization(OrganizationParam organizationParam);

  @POST
  @Path("move-organization")
  Response<Void> moveOrganization(OrganizationParam organizationParam);
}
