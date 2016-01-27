package com.dianrong.common.uniauth.server.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.dianrong.common.uniauth.server.data.entity.RolePermissionExample;
import com.dianrong.common.uniauth.server.data.entity.RolePermissionKey;
import com.dianrong.common.uniauth.server.data.entity.ext.PermissionExt;
import com.dianrong.common.uniauth.server.data.entity.ext.RoleExt;
import com.dianrong.common.uniauth.server.data.mapper.PermTypeMapper;
import com.dianrong.common.uniauth.server.data.mapper.PermissionMapper;
import com.dianrong.common.uniauth.server.data.mapper.RoleMapper;
import com.dianrong.common.uniauth.server.data.mapper.RolePermissionMapper;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.server.util.CheckEmpty;

@RestController
public class PermissionResource implements IPermissionRWResource {

	@Autowired
	private PermTypeMapper permTypeMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
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
		permissionDto.setPermType(permType.getType());
		
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
		Integer domainId = permissionParam.getDomainId();
		Integer permissionId = permissionParam.getId();
		CheckEmpty.checkEmpty(domainId, "域ID");
		CheckEmpty.checkEmpty(permissionId, "权限ID");
		List<RoleExt> roleExtList = roleMapper.selectAllRolesByDomainId(domainId);
		List<RoleDto> roleDtoList = new ArrayList<RoleDto>();
		
		if(roleExtList != null && !roleExtList.isEmpty()){
			for(RoleExt roleExt : roleExtList){
				RoleDto roleDto = BeanConverter.convert(roleExt);
				roleDto.setChecked(permissionId.equals(roleDto.getPermissionId()));
				roleDtoList.add(roleDto);
			}
		}
		
		return new Response<List<RoleDto>>(roleDtoList);
	}

	@Override
	public Response<Void> saveRolesToPerm(PermissionParam permissionParam) {
		Integer domainId = permissionParam.getDomainId();
		Integer permissionId = permissionParam.getId();
		CheckEmpty.checkEmpty(domainId, "域ID");
		CheckEmpty.checkEmpty(permissionId, "权限ID");
		
		RolePermissionExample example = new RolePermissionExample();
		example.createCriteria().andPermissionIdEqualTo(permissionId);
		
		rolePermissionMapper.deleteByExample(example);
		
		List<RoleDto> roleDtoList = permissionParam.getRoleList();
		if(roleDtoList != null && !roleDtoList.isEmpty()){
			for(RoleDto roleDto : roleDtoList){
				Integer roleId = roleDto.getId();
				RolePermissionKey rolePermissionKey = new RolePermissionKey();
				rolePermissionKey.setRoleId(roleId);
				rolePermissionKey.setPermissionId(permissionId);
				rolePermissionMapper.insert(rolePermissionKey);
			}
		}
		return Response.success();
	}

	@Override
	public Response<PageDto<PermissionDto>> searchPerm(PermissionQuery permissionQuery) {
		Integer domainId = permissionQuery.getDomainId();
		CheckEmpty.checkEmpty(domainId, "域ID");
		
	    Integer pageOffset = permissionQuery.getPageNumber();
	    Integer pageSize = permissionQuery.getPageSize();
	    CheckEmpty.checkEmpty(pageOffset, "ageOffset");
		CheckEmpty.checkEmpty(pageOffset, "pageSize");
		
		Integer startIndex = pageOffset * pageSize;
		
		PermissionExt permissionExt = BeanConverter.convert(permissionQuery);
		Integer totalCount = permissionMapper.countByExampleForSearch(permissionExt);
		
		permissionExt.setStartIndex(startIndex);
		permissionExt.setWantCount(pageSize);
		
		List<Permission> permissionList = permissionMapper.selectByExampleForSearch(permissionExt);
		
		PageDto<PermissionDto> pageDto = new PageDto<PermissionDto>();
		pageDto.setCurrentPage(pageOffset);
		pageDto.setPageSize(pageSize);
		pageDto.setTotalCount(totalCount);
		
		Map<Integer, PermType> permTypeMap = getPermTypeMap();
		
		List<PermissionDto> permissionDtoList = new ArrayList<PermissionDto>();
		if(permissionList != null && !permissionList.isEmpty()){
			for(Permission permission: permissionList){
				PermissionDto permissionDto = BeanConverter.convert(permission);
				Integer permTypeId = permissionDto.getPermTypeId();
				PermType permType = permTypeMap.get(permTypeId);
				
				permissionDto.setPermType(permType.getType());
				permissionDtoList.add(permissionDto);
			}
		}
		pageDto.setData(permissionDtoList);
		
		return new Response<PageDto<PermissionDto>>(pageDto);
	}
	
	private Map<Integer, PermType> getPermTypeMap(){
		Map<Integer, PermType>	permTypeMap = new HashMap<Integer, PermType>();
		PermTypeExample example = new PermTypeExample();
		List<PermType> permTypeList = permTypeMapper.selectByExample(example);
		if(permTypeList != null && !permTypeList.isEmpty()){
			for(PermType permType: permTypeList){
				permTypeMap.put(permType.getId(), permType);
			}
		}
		return permTypeMap;
	}
}
