package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import lombok.ToString;

import java.util.List;

@ToString
@ApiModel("组操作请求参数")
public class GroupParam extends PageParam {

  private static final long serialVersionUID = -4261321484001881493L;

  private Integer id;
  private String code;
  private String name;
  private String description;
  private Byte status;
  private Byte userStatus;

  // when add
  private Integer targetGroupId;
  // when delete
  private List<Integer> targetGroupIds;
  // if true only group, ignore members under each group
  // if false, both group and members returned
  private Boolean onlyShowGroup;
  // if onlyShowGroup=false and userGroupType=0, then return members, if if onlyShowGroup=false
  // and userGroupType=1 return owners
  private Byte userGroupType;

  private Boolean needOwnerMarkup;

  private Integer roleId;

  private Integer tagId;

  private Integer domainId;

  private List<Integer> roleIds;

  private List<Integer> tagIds;

  private Boolean onlyNeedGrpInfo;

  // 是否包含组的角色信息
  private Boolean needGrpRole;

  // 是否包含组的扩展属性信息
  private Boolean needGrpExtendVal;

  // 是否包含组的标签信息
  private Boolean needGrpTag;

  // 是否包含组关联的用户信息
  private Boolean needGrpUser;

  // 在返回的user信息列表中是否包含禁用的用户信息.
  private Boolean includeDisableUser;

  // 是否包含组关联用户的角色信息
  private Boolean needGrpUserRole;

  // 是否包含组关联用户的标签信息
  private Boolean needGrpUserTag;

  // 是否包含组关联用户的扩展信息
  private Boolean needGrpUserExtendVal;

  public Boolean getNeedGrpRole() {
    return needGrpRole;
  }

  public GroupParam setNeedGrpRole(Boolean needGrpRole) {
    this.needGrpRole = needGrpRole;
    return this;
  }

  public Boolean getNeedGrpExtendVal() {
    return needGrpExtendVal;
  }

  public GroupParam setNeedGrpExtendVal(Boolean needGrpExtendVal) {
    this.needGrpExtendVal = needGrpExtendVal;
    return this;
  }

  public Boolean getNeedGrpTag() {
    return needGrpTag;
  }

  public GroupParam setNeedGrpTag(Boolean needGrpTag) {
    this.needGrpTag = needGrpTag;
    return this;
  }

  public Boolean getNeedGrpUser() {
    return needGrpUser;
  }

  public GroupParam setNeedGrpUser(Boolean needGrpUser) {
    this.needGrpUser = needGrpUser;
    return this;
  }

  public Boolean getNeedGrpUserRole() {
    return needGrpUserRole;
  }

  public GroupParam setNeedGrpUserRole(Boolean needGrpUserRole) {
    this.needGrpUserRole = needGrpUserRole;
    return this;
  }

  public Boolean getNeedGrpUserTag() {
    return needGrpUserTag;
  }

  public GroupParam setNeedGrpUserTag(Boolean needGrpUserTag) {
    this.needGrpUserTag = needGrpUserTag;
    return this;
  }

  public Boolean getNeedGrpUserExtendVal() {
    return needGrpUserExtendVal;
  }

  public GroupParam setNeedGrpUserExtendVal(Boolean needGrpUserExtendVal) {
    this.needGrpUserExtendVal = needGrpUserExtendVal;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public GroupParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public GroupParam setCode(String code) {
    this.code = code;
    return this;
  }

  public String getName() {
    return name;
  }

  public GroupParam setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public GroupParam setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public GroupParam setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Integer getTargetGroupId() {
    return targetGroupId;
  }

  public GroupParam setTargetGroupId(Integer targetGroupId) {
    this.targetGroupId = targetGroupId;
    return this;
  }

  public Boolean getOnlyShowGroup() {
    return onlyShowGroup;
  }

  public GroupParam setOnlyShowGroup(Boolean onlyShowGroup) {
    this.onlyShowGroup = onlyShowGroup;
    return this;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public GroupParam setRoleId(Integer roleId) {
    this.roleId = roleId;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public GroupParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public List<Integer> getRoleIds() {
    return roleIds;
  }

  public GroupParam setRoleIds(List<Integer> roleIds) {
    this.roleIds = roleIds;
    return this;
  }

  public Byte getUserGroupType() {
    return userGroupType;
  }

  public GroupParam setUserGroupType(Byte userGroupType) {
    this.userGroupType = userGroupType;
    return this;
  }

  public Boolean getNeedOwnerMarkup() {
    return needOwnerMarkup;
  }

  public GroupParam setNeedOwnerMarkup(Boolean needOwnerMarkup) {
    this.needOwnerMarkup = needOwnerMarkup;
    return this;
  }

  public List<Integer> getTargetGroupIds() {
    return targetGroupIds;
  }

  public GroupParam setTargetGroupIds(List<Integer> targetGroupIds) {
    this.targetGroupIds = targetGroupIds;
    return this;
  }

  public Integer getTagId() {
    return tagId;
  }

  public GroupParam setTagId(Integer tagId) {
    this.tagId = tagId;
    return this;
  }

  public List<Integer> getTagIds() {
    return tagIds;
  }

  public GroupParam setTagIds(List<Integer> tagIds) {
    this.tagIds = tagIds;
    return this;
  }

  public Boolean getOnlyNeedGrpInfo() {
    return onlyNeedGrpInfo;
  }

  public GroupParam setOnlyNeedGrpInfo(Boolean onlyNeedGrpInfo) {
    this.onlyNeedGrpInfo = onlyNeedGrpInfo;
    return this;
  }

  public Boolean getIncludeDisableUser() {
    return includeDisableUser;
  }

  public GroupParam setIncludeDisableUser(Boolean includeDisableUser) {
    this.includeDisableUser = includeDisableUser;
    return this;
  }

  public Byte getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(Byte userStatus) {
    this.userStatus = userStatus;
  }
}
