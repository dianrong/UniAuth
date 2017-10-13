package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("permission")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IPermissionResource {

  @GET
  @Path("allpermtypecodes")
  // scenario: get all permission type codes, dictionary data from perm_type table
  Response<List<PermTypeDto>> getAllPermTypeCodes();

  @POST
  @Path("searchperms")
  // scenario: search permissions
  Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery);

  @POST
  @Path("roles")
  // scenario: retrieve all roles connected with a permission(including all other roles under a
  // domain)
  Response<List<RoleDto>> getAllRolesToPerm(PermissionParam permissionParam);

  @POST
  @Path("urlrolemappings")
  // scenario: populate url and role mappings for ss-client project
  Response<List<UrlRoleMappingDto>> getUrlRoleMapping(DomainParam domainParam);

  @POST
  @Path("search-users-by-perms")
  Response<List<PermissionDto>> searchUsersByPerms(PermissionQuery permissionQuery);
}
