package com.dianrong.common.uniauth.server.data.mapper;

import com.dianrong.common.uniauth.server.data.entity.RolePermissionHolder;
import java.util.List;
import java.util.Map;

public interface LoginMapper {

  List<RolePermissionHolder> selectRolePermission(Map<String, Object> map);
}
