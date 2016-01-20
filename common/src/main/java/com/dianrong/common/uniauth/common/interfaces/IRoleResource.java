package com.dianrong.common.uniauth.common.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;

@Path("role")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IRoleResource {
    @GET
    @Path("allrolecodes")
    //scenario: get all role codes
    Response<List<RoleCodeDto>> getAllRoleCodes();
	
    @POST
    @Path("searchroles")
    //scenario: search role
    Response<List<RoleDto>> searchRole(RoleQuery roleQuery);
    
    @POST
    @Path("addnewrole")
    //scenario: add new role
    Response<RoleDto> addNewRole(RoleParam roleParam);
    
    @POST
    @Path("updaterole")
    //scenario: update role
    Response<String> updateRole(RoleParam roleParam);
    
    @POST
    @Path("deleterole")
    //scenario: delete role
    Response<String> deleteRole(PrimaryKeyParam primaryKeyParam);
}
