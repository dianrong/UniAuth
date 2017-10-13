package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.server.service.PermissionService;
import com.dianrong.common.uniauth.server.support.audit.ResourceAudit;
import com.dianrong.common.uniauth.sharerw.interfaces.IPermissionRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api("权限信息操作接口")
@RestController
public class PermissionResource implements IPermissionRWResource {

  @Autowired
  private PermissionService permissionService;

  @Override
  @Timed
  public Response<List<PermTypeDto>> getAllPermTypeCodes() {
    List<PermTypeDto> permTypeDtoList = permissionService.getAllPermTypeCodes();
    return new Response<List<PermTypeDto>>(permTypeDtoList);
  }

  @ResourceAudit
  @Override
  public Response<PermissionDto> addNewPerm(PermissionParam permissionParam) {
    PermissionDto permissionDto = permissionService.addNewPerm(permissionParam);
    return new Response<PermissionDto>(permissionDto);
  }

  @ResourceAudit
  @Override
  public Response<Void> updatePerm(PermissionParam permissionParam) {
    permissionService.updatePerm(permissionParam);
    return Response.success();
  }

  @ResourceAudit
  @Override
  public Response<Void> replaceRolesToPerm(PermissionParam permissionParam) {
    permissionService.replaceRolesToPerm(permissionParam.getId(), permissionParam.getRoleIds());
    return Response.success();
  }

  @Override
  @Timed
  public Response<List<RoleDto>> getAllRolesToPerm(PermissionParam permissionParam) {
    List<RoleDto> roleDtoList = permissionService
        .getAllRolesToPermUnderADomain(permissionParam.getDomainId(), permissionParam.getId());
    return new Response<List<RoleDto>>(roleDtoList);
  }

  @ResourceAudit
  @Override
  public Response<Void> saveRolesToPerm(PermissionParam permissionParam) {
    permissionService.saveRolesToPerm(permissionParam);
    return Response.success();
  }

  @Override
  @Timed
  public Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
    PageDto<PermissionDto> pageDto = permissionService.searchPerm(permissionQuery.getPermIds(),
        permissionQuery.getId(), permissionQuery.getDomainId(), permissionQuery.getStatus(),
        permissionQuery.getValueExt(), permissionQuery.getValue(), permissionQuery.getPermTypeId(),
        permissionQuery.getPageNumber(), permissionQuery.getPageSize());
    return new Response<PageDto<PermissionDto>>(pageDto);
  }

  @Override
  public Response<List<UrlRoleMappingDto>> getUrlRoleMapping(DomainParam domainParam) {
    List<UrlRoleMappingDto> urlRoleMappingDtoList =
        permissionService.getUrlRoleMapping(domainParam);
    return new Response<List<UrlRoleMappingDto>>(urlRoleMappingDtoList);
  }

  @ApiOperation("根据权限列表查询关联的用户信息列表(没有权限和组的关联信息)")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "permIds", value = "指定的权限id列表", required = true,
          dataType = "java.util.List", paramType = "query"),
      @ApiImplicitParam(name = "includePermRole", value = "是否包含权限和角色关联关系信息", defaultValue = "false",
          dataType = "boolean", paramType = "query"),
      @ApiImplicitParam(name = "includeRoleUser", value = "是否包含角色和用户关联关系信息", defaultValue = "false",
          dataType = "boolean", paramType = "query")})
  @Override
  public Response<List<PermissionDto>> searchUsersByPerms(PermissionQuery permissionQuery) {
    return Response.success(permissionService.searchUsersByPerms(permissionQuery.getPermIds(),
        permissionQuery.getIncludePermRole(), permissionQuery.getIncludeRoleUser(),
        permissionQuery.getIncludeDisableUser()));
  }
}
