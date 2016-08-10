package com.dianrong.common.uniauth.server.data.mapper;

import java.util.List;
import java.util.Map;

import com.dianrong.common.uniauth.server.data.entity.RolePermissionHolder;

public interface LoginMapper {
	
	
	List<RolePermissionHolder> selectRolePermission(Map map);
	

}
