package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.server.service.PermissionService;
import com.dianrong.common.uniauth.sharerw.interfaces.IPermissionRWResource;
import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

  @Override
  public Response<PermissionDto> addNewPerm(PermissionParam permissionParam) {
    PermissionDto permissionDto = permissionService.addNewPerm(permissionParam);
    return new Response<PermissionDto>(permissionDto);
  }

  @Override
  public Response<Void> updatePerm(PermissionParam permissionParam) {
    permissionService.updatePerm(permissionParam);
    return Response.success();
  }

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

}
