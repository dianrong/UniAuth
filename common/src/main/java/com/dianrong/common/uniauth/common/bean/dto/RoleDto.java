package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.ToString;

/**
 * Created by Arc on 14/1/16.
 */
@ToString
@ApiModel("角色信息")
public class RoleDto extends TenancyBaseDto {

  private static final long serialVersionUID = 1578124776064866724L;

  @ApiModelProperty("主键id")
  private Integer id;

  @ApiModelProperty("名称")
  private String name;

  @ApiModelProperty("描述")
  private String description;

  @ApiModelProperty("状态(0,1)")
  private Byte status;

  @ApiModelProperty("角色所在域的id")
  private Integer domainId;

  @ApiModelProperty("角色类型的id")
  private Integer roleCodeId;

  private Integer permissionId;

  @ApiModelProperty("对应的域信息")
  private DomainDto domain;
  private String roleCode;

  private List<GroupDto> groupList;
  private Map<String, Set<String>> permMap;
  private Map<String, Set<PermissionDto>> permDtoMap;

  // whether this role connected with this permission
  // private boolean permChecked;
  // whether this role connected with this user
  // private boolean roleChecked;
  // whether this role connected with this group
  // private boolean groupChecked;

  // whether this role connected with a permission, user or group
  private Boolean checked;

  public Integer getPermissionId() {
    return permissionId;
  }

  public RoleDto setPermissionId(Integer permissionId) {
    this.permissionId = permissionId;
    return this;
  }

  public Integer getRoleCodeId() {
    return roleCodeId;
  }

  public RoleDto setRoleCodeId(Integer roleCodeId) {
    this.roleCodeId = roleCodeId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public RoleDto setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public RoleDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RoleDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public RoleDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public RoleDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public DomainDto getDomain() {
    return domain;
  }

  public RoleDto setDomain(DomainDto domain) {
    this.domain = domain;
    return this;
  }

  public String getRoleCode() {
    return roleCode;
  }

  public RoleDto setRoleCode(String roleCode) {
    this.roleCode = roleCode;
    return this;
  }

  public List<GroupDto> getGroupList() {
    return groupList;
  }

  public RoleDto setGroupList(List<GroupDto> groupList) {
    this.groupList = groupList;
    return this;
  }


  public Map<String, Set<String>> getPermMap() {
    return permMap;
  }

  public RoleDto setPermMap(Map<String, Set<String>> permMap) {
    this.permMap = permMap;
    return this;
  }

  public Boolean getChecked() {
    return checked;
  }

  public RoleDto setChecked(Boolean checked) {
    this.checked = checked;
    return this;
  }

  public Map<String, Set<PermissionDto>> getPermDtoMap() {
    return permDtoMap;
  }

  public RoleDto setPermDtoMap(Map<String, Set<PermissionDto>> permDtoMap) {
    this.permDtoMap = permDtoMap;
    return this;
  }
}
