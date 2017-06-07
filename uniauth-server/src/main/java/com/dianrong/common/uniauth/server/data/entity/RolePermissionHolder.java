package com.dianrong.common.uniauth.server.data.entity;

public class RolePermissionHolder {

  // =====role begin========
  private Integer roleId;
  private String roleName;
  private String roleDescription;
  private Byte roleStatus;
  private Integer roleCodeId;
  // =====role end=========


  // =====permission begin========
  private Integer permissionId;

  private String permissionValue;
  private String permissionValueExt;
  private String permissionDescription;
  private Byte permissionStatus;
  private Integer permTypeId;
  private Integer domainId;
  // =====permission end=======


  public Integer getRoleId() {
    return roleId;
  }

  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleDescription() {
    return roleDescription;
  }

  public void setRoleDescription(String roleDescription) {
    this.roleDescription = roleDescription;
  }

  public Byte getRoleStatus() {
    return roleStatus;
  }

  public void setRoleStatus(Byte roleStatus) {
    this.roleStatus = roleStatus;
  }

  public Integer getRoleCodeId() {
    return roleCodeId;
  }

  public void setRoleCodeId(Integer roleCodeId) {
    this.roleCodeId = roleCodeId;
  }

  public Integer getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Integer permissionId) {
    this.permissionId = permissionId;
  }

  public String getPermissionValue() {
    return permissionValue;
  }

  public void setPermissionValue(String permissionValue) {
    this.permissionValue = permissionValue;
  }

  public String getPermissionValueExt() {
    return permissionValueExt;
  }

  public void setPermissionValueExt(String permissionValueExt) {
    this.permissionValueExt = permissionValueExt;
  }

  public String getPermissionDescription() {
    return permissionDescription;
  }

  public void setPermissionDescription(String permissionDescription) {
    this.permissionDescription = permissionDescription;
  }

  public Byte getPermissionStatus() {
    return permissionStatus;
  }

  public void setPermissionStatus(Byte permissionStatus) {
    this.permissionStatus = permissionStatus;
  }

  public Integer getPermTypeId() {
    return permTypeId;
  }

  public void setPermTypeId(Integer permTypeId) {
    this.permTypeId = permTypeId;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public void setDomainId(Integer domainId) {
    this.domainId = domainId;
  }
}
