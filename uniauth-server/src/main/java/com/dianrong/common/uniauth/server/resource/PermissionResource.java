package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermTypeDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.PermissionParam;
import com.dianrong.common.uniauth.common.bean.request.PermissionQuery;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.interfaces.rw.IPermissionRWResource;
import com.dianrong.common.uniauth.server.data.entity.PermType;
import com.dianrong.common.uniauth.server.data.entity.PermTypeExample;
import com.dianrong.common.uniauth.server.data.entity.Permission;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

@RestController
public class PermissionResource implements IPermissionRWResource {

	@Autowired
	private PermTypeMapper permTypeMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	
	@Override
	public Response<List<PermTypeDto>> getAllPermTypeCodes() {
		PermTypeExample example = new PermTypeExample();
		List<PermType> permTypeList = permTypeMapper.selectByExample(example);
		List<PermTypeDto> permTypeDtoList = new ArrayList<PermTypeDto>();
		if(permTypeList != null && !permTypeList.isEmpty()){
			for(PermType pt: permTypeList){
				permTypeDtoList.add(BeanConverter.convert(pt));
			}
		}
		
		return new Response<List<PermTypeDto>>(permTypeDtoList);
	}

	@Override
	public Response<PermissionDto> addNewPerm(PermissionParam permissionParam) {
		Integer domainId = permissionParam.getDomainId();
		Integer permTypeId = permissionParam.getPermTypeId();
		String value = permissionParam.getValue();
		CheckEmpty.checkEmpty(domainId, "域ID");
		CheckEmpty.checkEmpty(permTypeId, "权限类型ID");
		CheckEmpty.checkEmpty(value, "权限的值");
		
		Permission permission = BeanConverter.convert(permissionParam, false);
		permissionMapper.insert(permission);
		
		PermissionDto permissionDto = BeanConverter.convert(permission);
		PermType permType = permTypeMapper.selectByPrimaryKey(permTypeId);
		PermTypeDto permTypeDto = BeanConverter.convert(permType);
		permissionDto.setPermTypeDto(permTypeDto);
		
		return new Response<PermissionDto>(permissionDto);
	}

	@Override
	public Response<Void> updatePerm(PermissionParam permissionParam) {
		Integer domainId = permissionParam.getDomainId();
		Integer permTypeId = permissionParam.getPermTypeId();
		String value = permissionParam.getValue();
		CheckEmpty.checkEmpty(domainId, "域ID");
		CheckEmpty.checkEmpty(permTypeId, "权限类型ID");
		CheckEmpty.checkEmpty(value, "权限的值");
		
		Permission permission = BeanConverter.convert(permissionParam, true);
		permissionMapper.updateByPrimaryKey(permission);
		
		
		return Response.success();
	}

	@Override
	public Response<Void> deletePerm(PrimaryKeyParam primaryKeyParam) {
		CheckEmpty.checkParamId(primaryKeyParam, "权限ID");
		permissionMapper.deleteByPrimaryKey(primaryKeyParam.getId());
		return Response.success();
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToPerm(PermissionParam permissionParam) {
		
		return null;
	}

	@Override
	public Response<Void> saveRolesToPerm(PermissionParam permissionParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
		// TODO Auto-generated method stub
		return null;
	}

}
