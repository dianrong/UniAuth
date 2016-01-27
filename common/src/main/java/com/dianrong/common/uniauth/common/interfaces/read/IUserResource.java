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
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
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
    @Path("domain/roles")
    //scenario: retrieve all roles connected with a user(including all other roles under a domain)
    Response<List<RoleDto>> getAllRolesToUserAndDomain(UserParam userParam);
}
