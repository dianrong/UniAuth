package com.dianrong.common.uniauth.common.bean.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 * Created by Arc on 3/3/2016.
 */
@ApiModel("组查询信息")
public class GroupQuery extends PageParam {

  private static final long serialVersionUID = -6174713703363466941L;

  @ApiModelProperty("主键id")
  private Integer id;

  @ApiModelProperty("辅助查询字段,组id列表")
  private List<Integer> groupIds;

  @ApiModelProperty("编码")
  private String code;

  @ApiModelProperty("组名")
  private String name;

  @ApiModelProperty("描述")
  private String description;

  @ApiModelProperty("状态(0,1)")
  private Byte status;

  @ApiModelProperty("用户组的关系类型(0,1)")
  private Byte userGroupType;

  @ApiModelProperty("用户id")
  private Long userId;

  @ApiModelProperty("角色id")
  private Integer roleId;

  @ApiModelProperty("标签id")
  private Integer tagId;

  @ApiModelProperty("是否需要查询tag信息")
  private Boolean needTag;

  @ApiModelProperty("是否需要查询user信息")
  private Boolean needUser;

  @ApiModelProperty("是否需要查询父组id")
  private Boolean needParentId;

  @ApiModelProperty("是否包含有owner关系的组")
  private Boolean includeOwner;

  @ApiModelProperty("是否查询非直属的父组信息")
  private Boolean includeIndirectAncestors;
  
  @ApiModelProperty("匹配名称或编码")
  private String codeName;

  public String getCodeName() {
    return codeName;
  }

  public GroupQuery setCodeName(String codeName) {
    this.codeName = codeName;
    return this;
  }

  public Boolean getIncludeIndirectAncestors() {
    return includeIndirectAncestors;
  }

  public GroupQuery setIncludeIndirectAncestors(Boolean includeIndirectAncestors) {
    this.includeIndirectAncestors = includeIndirectAncestors;
    return this;
  }

  public List<Integer> getGroupIds() {
    return groupIds;
  }

  public GroupQuery setGroupIds(List<Integer> groupIds) {
    this.groupIds = groupIds;
    return this;
  }

  public Integer getTagId() {
    return tagId;
  }

  public GroupQuery setTagId(Integer tagId) {
    this.tagId = tagId;
    return this;
  }

  public Integer getId() {
    return id;
  }

  public GroupQuery setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public GroupQuery setCode(String code) {
    this.code = code;
    return this;
  }

  public String getName() {
    return name;
  }

  public GroupQuery setName(String name) {
    this.name = name;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public GroupQuery setDescription(String description) {
    this.description = description;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public GroupQuery setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Byte getUserGroupType() {
    return userGroupType;
  }

  public GroupQuery setUserGroupType(Byte userGroupType) {
    this.userGroupType = userGroupType;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public GroupQuery setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Boolean getNeedTag() {
    return needTag;
  }

  public GroupQuery setNeedTag(Boolean needTag) {
    this.needTag = needTag;
    return this;
  }

  public Boolean getNeedUser() {
    return needUser;
  }

  public GroupQuery setNeedUser(Boolean needUser) {
    this.needUser = needUser;
    return this;
  }

  public Integer getRoleId() {
    return roleId;
  }

  public GroupQuery setRoleId(Integer roleId) {
    this.roleId = roleId;
    return this;
  }

  public Boolean getNeedParentId() {
    return needParentId;
  }

  public GroupQuery setNeedParentId(Boolean needParentId) {
    this.needParentId = needParentId;
    return this;
  }

  public Boolean getIncludeOwner() {
    return includeOwner;
  }

  public GroupQuery setIncludeOwner(Boolean includeOwner) {
    this.includeOwner = includeOwner;
    return this;
  }

  @Override
  public String toString() {
    return "GroupQuery [id=" + id + ", groupIds=" + groupIds + ", code=" + code + ", name=" + name
        + ", description=" + description + ", status=" + status + ", userGroupType="
        + userGroupType + ", userId=" + userId + ", roleId=" + roleId + ", tagId=" + tagId
        + ", needTag=" + needTag + ", needUser=" + needUser + ", needParentId="
        + needParentId + ", includeOwner=" + includeOwner + ", includeIndirectAncestors="
        + includeIndirectAncestors + "]";
  }
}
