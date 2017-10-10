package com.dianrong.common.uniauth.common.bean.dto;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class PermissionDto extends TenancyBaseDto {

  private static final long serialVersionUID = -3799602677526320793L;
  private Integer id;
  private String value;
  private String valueExt;
  private String description;
  private Byte status;
  private Integer permTypeId;
  private Integer domainId;

  private String permType;

  // whether this permission connected with this role
  private Boolean checked;

  // Permission->UserList
  private List<UserDto> relatedUsers;
  // Permission->RoleList
  private List<RoleDto> relatedRoles;
  // Role->UserList
  private Map<Integer, List<UserDto>> roleUserListMap;

  public List<UserDto> getRelatedUsers() {
    return relatedUsers;
  }

  public PermissionDto setRelatedUsers(List<UserDto> relatedUsers) {
    this.relatedUsers = relatedUsers;
    return this;
  }

  public List<RoleDto> getRelatedRoles() {
    return relatedRoles;
  }

  public PermissionDto setRelatedRoles(List<RoleDto> relatedRoles) {
    this.relatedRoles = relatedRoles;
    return this;
  }

  public Map<Integer, List<UserDto>> getRoleUserListMap() {
    return roleUserListMap;
  }

  public PermissionDto setRoleUserListMap(Map<Integer, List<UserDto>> roleUserListMap) {
    this.roleUserListMap = roleUserListMap;
    return this;
  }

  public String getPermType() {
    return permType;
  }

  public PermissionDto setPermType(String permType) {
    this.permType = permType;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public PermissionDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getValue() {
    return value;
  }

  public PermissionDto setValue(String value) {
    this.value = value;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public PermissionDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public PermissionDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getPermTypeId() {
    return permTypeId;
  }

  public PermissionDto setPermTypeId(Integer permTypeId) {
    this.permTypeId = permTypeId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public PermissionDto setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public Boolean getChecked() {
    return checked;
  }

  public PermissionDto setChecked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  public String getValueExt() {
    return valueExt;
  }

  public PermissionDto setValueExt(String valueExt) {
    this.valueExt = valueExt;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PermissionDto that = (PermissionDto) o;

    if (id != null ? !id.equals(that.id) : that.id != null) {
      return false;
    }
    if (value != null ? !value.equals(that.value) : that.value != null) {
      return false;
    }
    if (valueExt != null ? !valueExt.equals(that.valueExt) : that.valueExt != null) {
      return false;
    }
    if (description != null ? !description.equals(that.description) : that.description != null) {
      return false;
    }
    if (status != null ? !status.equals(that.status) : that.status != null) {
      return false;
    }
    if (permTypeId != null ? !permTypeId.equals(that.permTypeId) : that.permTypeId != null) {
      return false;
    }
    if (domainId != null ? !domainId.equals(that.domainId) : that.domainId != null) {
      return false;
    }
    if (permType != null ? !permType.equals(that.permType) : that.permType != null) {
      return false;
    }
    return !(checked != null ? !checked.equals(that.checked) : that.checked != null);

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    result = 31 * result + (valueExt != null ? valueExt.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (permTypeId != null ? permTypeId.hashCode() : 0);
    result = 31 * result + (domainId != null ? domainId.hashCode() : 0);
    result = 31 * result + (permType != null ? permType.hashCode() : 0);
    result = 31 * result + (checked != null ? checked.hashCode() : 0);
    return result;
  }
}
