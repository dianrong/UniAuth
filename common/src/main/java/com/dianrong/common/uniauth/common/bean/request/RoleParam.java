package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class RoleParam extends Operator {

  private static final long serialVersionUID = -1555945847793133921L;
  private Integer id;
  private String name;
  private String description;
  private Byte status;
  private Integer roleCodeId;
  private Integer domainId;
  private List<Integer> permIds;
  private List<Integer> grpIds;
  private List<Long> userIds;

  // 默认是全部都要处理
  private Boolean needProcessGoupIds = Boolean.TRUE;
  private Boolean needProcessUserIds = Boolean.TRUE;

  public Boolean getNeedProcessGoupIds() {
    return needProcessGoupIds;
  }

  public void setNeedProcessGoupIds(Boolean needProcessGoupIds) {
    this.needProcessGoupIds = needProcessGoupIds;
  }

  public Boolean getNeedProcessUserIds() {
    return needProcessUserIds;
  }

  public void setNeedProcessUserIds(Boolean needProcessUserIds) {
    this.needProcessUserIds = needProcessUserIds;
  }

  public Integer getId() {
    return id;
  }

  public RoleParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public RoleParam setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public RoleParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public RoleParam setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getRoleCodeId() {
    return roleCodeId;
  }

  public RoleParam setRoleCodeId(Integer roleCodeId) {
    this.roleCodeId = roleCodeId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public RoleParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public List<Integer> getPermIds() {
    return permIds;
  }

  public RoleParam setPermIds(List<Integer> permIds) {
    this.permIds = permIds;
    return this;
  }

  public List<Integer> getGrpIds() {
    return grpIds;
  }

  public RoleParam setGrpIds(List<Integer> grpIds) {
    this.grpIds = grpIds;
    return this;
  }

  public List<Long> getUserIds() {
    return userIds;
  }

  public RoleParam setUserIds(List<Long> userIds) {
    this.userIds = userIds;
    return this;
  }

  @Override
  public String toString() {
    return "RoleParam [id=" + id + ", name=" + name + ", description=" + description + ", status="
        + status + ", roleCodeId=" + roleCodeId + ", domainId=" + domainId
        + ", permIds=" + permIds + ", grpIds=" + grpIds + ", userIds=" + userIds
        + ", needProcessGoupIds=" + needProcessGoupIds + ", needProcessUserIds="
        + needProcessUserIds + "]";
  }
}
