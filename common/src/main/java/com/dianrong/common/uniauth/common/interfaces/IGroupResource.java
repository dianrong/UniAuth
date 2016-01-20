package com.dianrong.common.uniauth.common.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;

@Path("group")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IGroupResource {
	
    @GET
    @Path("tree")
    //scenario: group tree
    Response<GroupDto> getGroupTree();
    
    @POST
    @Path("addusers")
    //scenario: add users into one specific group(normal member and owner member)
    Response<String> addUsersIntoGroup(UserListParam userListParam);
    
    @POST
    @Path("removeusers")
    //scenario: remove users from one specific group(normal member and owner member)
    Response<String> removeUsersFromGroup(UserListParam userListParam);
    
    @POST
    @Path("addnewgroup")
    //scenario: add new group into one specific group
    Response<GroupDto> addNewGroupIntoGroup(GroupParam groupParam);
    
    @POST
    @Path("deletegroup")
    //scenario: delete group
    Response<String> deleteGroup(PrimaryKeyParam primaryKeyParam);
    
    @POST
    @Path("updategroup")
    //scenario: modify group info
    Response<String> updateGroup(GroupParam groupParam);
    
    @POST
    @Path("groupowners")
    //scenario: get all group owners
    Response<List<UserDto>> getGroupOwners(PrimaryKeyParam primaryKeyParam);
}
