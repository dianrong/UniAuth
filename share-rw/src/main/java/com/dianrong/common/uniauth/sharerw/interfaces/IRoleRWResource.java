package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.interfaces.read.IRoleResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;


public interface IRoleRWResource extends IRoleResource {

  @POST
  @Path("addnewrole")
  // scenario: add new role
  Response<RoleDto> addNewRole(RoleParam roleParam);

  @POST
  @Path("updaterole")
  // scenario: update role
  Response<Void> updateRole(RoleParam roleParam);

  @POST
  @Path("replacepermtorole")
  Response<Void> replacePermsToRole(RoleParam roleParam);

  @POST
  @Path("savepermstorole")
  // scenario: save permissions to a role
  Response<Void> savePermsToRole(RoleParam roleParam);

  @POST
  @Path("replace-grps-users-to-role")
  Response<Void> replaceGroupsAndUsersToRole(RoleParam roleParam);

  @POST
  @Path("relate-users-and-role")
  Response<Void> relateUsersAndRole(RoleParam roleParam);
}
