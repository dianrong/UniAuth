package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.GroupQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("group")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IGroupResource {
	
    @POST
    @Path("tree")
    //scenario: group/member tree
    //if groupId == null then retrieve from the root tree, otherwise treat the groupId as the root tree.
    Response<GroupDto> getGroupTree(GroupParam groupParam);

    @POST
    @Path("query")
    Response<PageDto<GroupDto>> queryGroup(GroupQuery groupQuery);

    @POST
    @Path("groupowners")
    //scenario: get all group owners
    Response<List<UserDto>> getGroupOwners(PrimaryKeyParam primaryKeyParam);
    
    @POST
    @Path("domain/roles")
    //scenario: retrieve all roles connected with a group(including all other roles under a domain)
    Response<List<RoleDto>> getAllRolesToGroupAndDomain(GroupParam groupParam);
}
