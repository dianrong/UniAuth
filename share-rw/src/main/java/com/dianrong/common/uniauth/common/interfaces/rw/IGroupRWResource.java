package com.dianrong.common.uniauth.common.interfaces.rw;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;

public interface IGroupRWResource extends IGroupResource {

    
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
    @Path("saverolestogroup")
    //scenario: save roles to group
    Response<String> saveRolesToGroup(GroupParam groupParam);
}
