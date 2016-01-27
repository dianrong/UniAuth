package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import com.dianrong.common.uniauth.server.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.GroupTreeParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.interfaces.rw.IRoleRWResource;
import com.dianrong.common.uniauth.server.data.entity.RoleCode;
import com.dianrong.common.uniauth.server.data.entity.RoleCodeExample;
import com.dianrong.common.uniauth.server.data.mapper.RoleCodeMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;

@RestController
public class RoleResource implements IRoleRWResource {
	
	@Autowired
	private RoleService roleService;

	@Override
	public Response<List<RoleCodeDto>> getAllRoleCodes() {
		List<RoleCodeDto> roleCodeDtos = roleService.getAllRoleCodes();
		return Response.success(roleCodeDtos);
	}

	@Override
	public Response<RoleDto> addNewRole(RoleParam roleParam) {
		RoleDto roleDto = roleService.addNewRole(roleParam.getDomainId(), roleParam.getRoleCodeId(),roleParam.getName(),roleParam.getDescription());
		return Response.success(roleDto);
	}

	@Override
	public Response<Void> updateRole(RoleParam roleParam) {
		roleService.updateRole(roleParam.getRoleActionEnum(), roleParam.getId(), roleParam.getRoleCodeId(),
				roleParam.getName(), roleParam.getDescription(), roleParam.getStatus());
		return Response.success();
	}

	@Override
	public Response<String> deleteRole(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<PermissionDto>> getAllPermsToRole(RoleParam roleParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> savePermsToRole(RoleParam roleParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> saveGroupTreeToRole(GroupTreeParam groupTreeParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<PageDto<RoleDto>> searchRole(RoleQuery roleQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
