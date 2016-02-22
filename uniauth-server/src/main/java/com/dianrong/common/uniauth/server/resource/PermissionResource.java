package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.sharerw.interfaces.IPermissionRWResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.server.service.PermissionService;

@RestController
public class PermissionResource implements IPermissionRWResource {

	@Autowired
	private PermissionService permissionService;
	
	@Override
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
	public Response<Void> deletePerm(PrimaryKeyParam primaryKeyParam) {
		permissionService.deletePerm(primaryKeyParam);
		return Response.success();
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToPerm(PermissionParam permissionParam) {
		List<RoleDto> roleDtoList = permissionService.getAllRolesToPerm(permissionParam);
		return new Response<List<RoleDto>>(roleDtoList);
	}

	@Override
	public Response<Void> saveRolesToPerm(PermissionParam permissionParam) {
		permissionService.saveRolesToPerm(permissionParam);
		return Response.success();
	}

	@Override
	public Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
		PageDto<PermissionDto> pageDto = permissionService.searchPerm(permissionQuery);
		
		return new Response<PageDto<PermissionDto>>(pageDto);
	}

	@Override
	public Response<List<UrlRoleMappingDto>> selectUrlRoleMapping(DomainParam domainParam) {
		List<UrlRoleMappingDto> urlRoleMappingDtoList = permissionService.selectUrlRoleMapping(domainParam);
		return new Response<List<UrlRoleMappingDto>>(urlRoleMappingDtoList);
	}
	
}
