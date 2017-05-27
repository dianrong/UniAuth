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
import com.dianrong.common.uniauth.sharerw.interfaces.IRoleRWResource;

import io.swagger.annotations.Api;
import java.util.List;

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
    List<RoleCodeDto> roleCodeDtos = roleService.getAllRoleCodes();
    return Response.success(roleCodeDtos);
  }

  @Override
  @Timed
  public Response<List<PermissionDto>> getAllPermsToRole(RoleParam roleParam) {
    List<PermissionDto> permissionDtos =
        roleService.getAllPermsToRole(roleParam.getDomainId(), roleParam.getId());
    return Response.success(permissionDtos);
  }

  @Override
  @Timed
  public Response<PageDto<RoleDto>> searchRole(RoleQuery roleQuery) {
    PageDto<RoleDto> roleDtos = roleService.searchRole(roleQuery.getRoleIds(), roleQuery.getId(),
        roleQuery.getDomainId(), roleQuery.getName(), roleQuery.getRoleCodeId(),
        roleQuery.getStatus(), roleQuery.getPageNumber(), roleQuery.getPageSize());
    return Response.success(roleDtos);
  }

  @Override
  public Response<RoleDto> addNewRole(RoleParam roleParam) {
    RoleDto roleDto = roleService.addNewRole(roleParam.getDomainId(), roleParam.getRoleCodeId(),
        roleParam.getName(), roleParam.getDescription());
    return Response.success(roleDto);
  }

  @Override
  public Response<Void> updateRole(RoleParam roleParam) {
    roleService.updateRole(roleParam.getId(), roleParam.getRoleCodeId(), roleParam.getName(),
        roleParam.getDescription(), roleParam.getStatus());
    return Response.success();
  }

  @Override
  public Response<Void> replacePermsToRole(RoleParam roleParam) {
    roleService.replacePermsToRole(roleParam.getId(), roleParam.getPermIds());
    return Response.success();
  }

  @Override
  public Response<Void> replaceGroupsAndUsersToRole(RoleParam roleParam) {
    roleService.replaceGroupsAndUsersToRole(roleParam.getId(), roleParam.getGrpIds(),
        roleParam.getUserIds(), roleParam.getNeedProcessGoupIds(),
        roleParam.getNeedProcessUserIds());
    return Response.success();
  }

  @Override
  public Response<Void> savePermsToRole(RoleParam roleParam) {
    roleService.savePermsToRole(roleParam.getId(), roleParam.getPermIds());
    return Response.success();
  }

}
