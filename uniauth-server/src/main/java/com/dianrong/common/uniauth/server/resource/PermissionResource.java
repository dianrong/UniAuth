package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.interfaces.read.IPermissionResource;
import com.dianrong.common.uniauth.common.interfaces.rw.IPermissionRWResource;

@RestController
public class PermissionResource implements IPermissionRWResource {

	@Override
	public Response<List<PermTypeDto>> getAllPermTypeCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<PermissionDto> addNewPerm(PermissionParam permissionParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> updatePerm(PermissionParam permissionParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> deletePerm(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToPerm(PermissionParam permissionParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> saveRolesToPerm(PermissionParam permissionParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
