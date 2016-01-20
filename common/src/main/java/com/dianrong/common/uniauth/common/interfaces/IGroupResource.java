package com.dianrong.common.uniauth.common.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
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
    @Path("adduser")
    //scenario: add users into one specified group
    Response<String> addUsersIntoGroup(UserListParam userListParam);
    
    @POST
    @Path("addgroup")
    //scenario: add group into one specified group
    Response<String> addGroupIntoGroup(GroupParam groupParam);
    
    @POST
    @Path("deletegroup")
    //scenario: delete group
    Response<String> deleteGroup(PrimaryKeyParam primaryKeyParam);
}
