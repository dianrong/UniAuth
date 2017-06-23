package com.dianrong.common.uniauth.server.data.entity.ext;

import com.dianrong.common.uniauth.server.data.entity.Role;

public class RoleExt extends Role {

  private String roleCode;
  private Integer permissionId;

  public String getRoleCode() {
    return roleCode;
  }

  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  public Integer getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Integer permissionId) {
    this.permissionId = permissionId;
  }
}
