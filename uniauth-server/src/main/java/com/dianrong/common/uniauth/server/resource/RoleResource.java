package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.interfaces.IRoleResource;

public class RoleResource implements IRoleResource {

	@Override
	public Response<List<RoleCodeDto>> getAllRoleCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<RoleDto>> searchRole(RoleQuery roleQuery) {
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

}
