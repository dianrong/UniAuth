package com.dianrong.common.uniauth.common.interfaces.read;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;

/**
 * Created by Arc on 14/1/16.
 */
@Path("user")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserResource {

    @POST
    @Path("searchusers")
    //scenario: search user
    Response<PageDto<UserDto>> searchUser(UserQuery userQuery);

    @POST
    @Path("userswithrolechecked")
    //scenario: search user with role checked
    Response<List<UserDto>> searchUsersWithRoleCheck(PrimaryKeyParam primaryKeyParam);

    @POST
    @Path("userswithtagchecked")
        //scenario: search user with role checked
    Response<List<UserDto>> searchUsersWithTagCheck(PrimaryKeyParam primaryKeyParam);
    
    @POST
    @Path("domain/roles")
    //scenario: retrieve all roles connected with a user(including all other roles under a domain)
    Response<List<RoleDto>> getAllRolesToUserAndDomain(UserParam userParam);

    @POST
    @Path("login")
    //scenario: cas login
    Response<UserDto> login(LoginParam loginParam);

    @POST
    @Path("usedetailinfo")
    //scenario: get user detail info
    Response<UserDetailDto> getUserDetailInfo(LoginParam loginParam);

    @POST
    @Path("usedetailinfobyid")
    //scenario: get user detail info by userId
    Response<UserDetailDto> getUserDetailInfoByUid(UserParam userParam);

    @POST
    @Path("singleuser")
    //scenario: cas login, check if the input email exists or not
    Response<UserDto> getSingleUser(UserParam userParam);
    
    @POST
    @Path("userInfobytag")
    //scenario: cas userinfo edit, check if the input email or phone exists
    Response<UserDto> getUserInfoByUserTag(LoginParam loginParam);
    
    @POST
    @Path("tagsWithUserChecked")
    //scenario: techops user-tag 
    Response<List<TagDto>>  getTagsWithUserCheckedInfo(UserParam userParam);
}
