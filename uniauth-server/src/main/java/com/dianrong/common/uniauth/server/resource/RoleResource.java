package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.GroupTreeParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.interfaces.read.IRoleResource;
import com.dianrong.common.uniauth.common.interfaces.rw.IRoleRWResource;

public class RoleResource implements IRoleRWResource {

	@Override
	public Response<List<RoleCodeDto>> getAllRoleCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<RoleDto> addNewRole(RoleParam roleParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> updateRole(RoleParam roleParam) {
		// TODO Auto-generated method stub
		return null;
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
