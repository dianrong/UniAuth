package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

public interface IGroupRWResource extends IGroupResource {
  
  @POST
  @Path("addusers")
  // scenario: add users into one specific group(normal member and owner member)
  Response<Void> addUsersIntoGroup(UserListParam userListParam);

  @POST
  @Path("removeusers")
  // scenario: remove users from one specific group(normal member and owner member)
  Response<Void> removeUsersFromGroup(UserListParam userListParam);

  // scenario: move users to the target group
  @POST
  @Path("moveUser")
  Response<Void> moveGroupUser(UserListParam userListParam);

  @POST
  @Path("addnewgroup")
  // scenario: add new group into one specific group
  Response<GroupDto> addNewGroupIntoGroup(GroupParam groupParam);

  @POST
  @Path("updategroup")
  // scenario: modify group info
  Response<GroupDto> updateGroup(GroupParam groupParam);

  @POST
  @Path("deleteGroup")
  Response<GroupDto> deleteGroup(GroupParam groupParam);

  @POST
  @Path("saverolestogroup")
  // scenario: save roles to group
  Response<Void> saveRolesToGroup(GroupParam groupParam);

  @POST
  @Path("replacerolestogroup")
  Response<Void> replaceRolesToGroup(GroupParam groupParam);

  @POST
  @Path("replacetagstogroup")
  // scenario: replace tags to group
  Response<Void> replaceTagsToGrp(GroupParam groupParam);

  @POST
  @Path("move")
  Response<Void> moveGroup(GroupParam groupParam);
}
