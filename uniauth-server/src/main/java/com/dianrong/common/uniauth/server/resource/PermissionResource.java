package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.interfaces.read.IPermissionResource;
import com.dianrong.common.uniauth.common.interfaces.rw.IPermissionRWResource;

public class PermissionResource implements IPermissionRWResource {

	@Override
	public Response<List<PermTypeDto>> getAllPermTypeCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
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

}
