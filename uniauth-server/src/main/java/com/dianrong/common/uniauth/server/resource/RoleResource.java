package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.server.service.RoleService;
import com.dianrong.common.uniauth.server.support.audit.ResourceAudit;
import com.dianrong.common.uniauth.sharerw.interfaces.IRoleRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


@Api("角色信息操作接口")
@RestController
public class RoleResource implements IRoleRWResource {

  @Autowired
  private RoleService roleService;

  @Override
  @Timed
  public Response<List<RoleCodeDto>> getAllRoleCodes() {
    return Response.success(roleService.getAllRoleCodes());
  }

  @Override
  @Timed
  public Response<List<PermissionDto>> getAllPermsToRole(RoleParam roleParam) {
    return Response
        .success(roleService.getAllPermsToRole(roleParam.getDomainId(), roleParam.getId()));
  }

  @ApiOperation("获取用户Id获取所有关联的角色(直接关联和通过组关联的),需要指定域")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "domainId", value = "域id", dataType = "int", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "userIds", value = "指定的角色列表", dataType = "java.util.List",
          required = true, paramType = "query")})
  @Override
  public Response<Map<Long, Set<RoleDto>>> getUserRoles(RoleParam roleParam) {
    return Response
        .success(roleService.getUserRoles(roleParam.getUserIds(), roleParam.getDomainId()));
  }

  @Override
  @Timed
  public Response<PageDto<RoleDto>> searchRole(RoleQuery roleQuery) {
    return Response.success(
        roleService.searchRole(roleQuery.getRoleIds(), roleQuery.getId(), roleQuery.getDomainId(),
            roleQuery.getName(), roleQuery.getRoleCodeId(), roleQuery.getStatus(),
            roleQuery.getNeedDomainInfo(), roleQuery.getPageNumber(), roleQuery.getPageSize()));
  }

  @ResourceAudit
  @Override
  public Response<RoleDto> addNewRole(RoleParam roleParam) {
    return Response
        .success(roleService.addNewRole(roleParam.getDomainId(), roleParam.getRoleCodeId(),
            roleParam.getName(), roleParam.getDescription()));
  }

  @ResourceAudit
  @Override
  public Response<Void> updateRole(RoleParam roleParam) {
    roleService.updateRole(roleParam.getId(), roleParam.getRoleCodeId(), roleParam.getName(),
        roleParam.getDescription(), roleParam.getStatus());
    return Response.success();
  }

  @ResourceAudit
  @Override
  public Response<Void> replacePermsToRole(RoleParam roleParam) {
    roleService.replacePermsToRole(roleParam.getId(), roleParam.getPermIds());
    return Response.success();
  }

  @ResourceAudit
  @Override
  public Response<Void> replaceGroupsAndUsersToRole(RoleParam roleParam) {
    roleService.replaceGroupsAndUsersToRole(roleParam.getId(), roleParam.getGrpIds(),
        roleParam.getUserIds(), roleParam.getNeedProcessGoupIds(),
        roleParam.getNeedProcessUserIds());
    return Response.success();
  }

  @ResourceAudit
  @ApiOperation("批量关联角色和权限")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "角色Id", dataType = "int", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "permIds", value = "需要关联到角色的权限id列表", dataType = "java.util.List",
          required = true, paramType = "query")})
  @Override
  public Response<Void> savePermsToRole(RoleParam roleParam) {
    roleService.savePermsToRole(roleParam.getId(), roleParam.getPermIds());
    return Response.success();
  }

  @ResourceAudit
  @ApiOperation("批量关联用户和角色")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "roleId", value = "角色Id", dataType = "int", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "userIds", value = "需要添加角色用户id列表", dataType = "java.util.List",
          required = true, paramType = "query")})
  @Override
  public Response<Void> relateUsersAndRole(RoleParam roleParam) {
    roleService.relateUsersAndRole(roleParam.getId(), roleParam.getUserIds());
    return Response.success();
  }
}
