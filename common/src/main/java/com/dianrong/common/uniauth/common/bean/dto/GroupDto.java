package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

import lombok.ToString;

@ToString
@ApiModel("组信息")
public class GroupDto extends TenancyBaseDto {

  private static final long serialVersionUID = 892184382591361189L;
  @ApiModelProperty("主键id")
  private Integer id;
  @ApiModelProperty("组code")
  private String code;
  @ApiModelProperty("组名称")
  private String name;
  @ApiModelProperty("组的创建时间")
  private Date createDate;
  @ApiModelProperty("组最近更新的时间")
  private Date lastUpdate;
  @ApiModelProperty("组的状态(0:启用,1:禁用)")
  private Byte status;
  @ApiModelProperty("组描述")
  private String description;
  @ApiModelProperty("组对应的标签信息")
  private List<TagDto> tags;
  @ApiModelProperty("父组id")
  private Integer parentId;
  @ApiModelProperty("与组关联的用户")
  private List<UserDto> users;
  private List<GroupDto> groups;

  // whether this group connected with a role
  @ApiModelProperty("辅助字段,判断组是否与某个角色有关联关系")
  private Boolean roleChecked;
  // whether this group connected with a tag
  @ApiModelProperty("辅助字段,判断组是否与某个标签有关联关系")
  private Boolean tagChecked;
  @ApiModelProperty("辅助字段,判断某个人是否是该组的owner")
  private Boolean ownerMarkup;
  @ApiModelProperty("是否是根组")
  private Boolean isRootGrp;

  public Integer getId() {
    return id;
  }

  public GroupDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public GroupDto setCode(String code) {
    this.code = code;
    return this;
  }

  public String getName() {
    return name;
  }

  public GroupDto setName(String name) {
    this.name = name;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public GroupDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public GroupDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public GroupDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public GroupDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public List<UserDto> getUsers() {
    return users;
  }

  public GroupDto setUsers(List<UserDto> users) {
    this.users = users;
    return this;
  }

  public List<GroupDto> getGroups() {
    return groups;
  }

  public GroupDto setGroups(List<GroupDto> groups) {
    this.groups = groups;
    return this;
  }

  public Boolean getRoleChecked() {
    return roleChecked;
  }

  public GroupDto setRoleChecked(Boolean roleChecked) {
    this.roleChecked = roleChecked;
    return this;
  }

  public Boolean getOwnerMarkup() {
    return ownerMarkup;
  }

  public GroupDto setOwnerMarkup(Boolean ownerMarkup) {
    this.ownerMarkup = ownerMarkup;
    return this;
  }

  public Boolean getTagChecked() {
    return tagChecked;
  }

  public GroupDto setTagChecked(Boolean tagChecked) {
    this.tagChecked = tagChecked;
    return this;
  }

  public List<TagDto> getTags() {
    return tags;
  }

  public GroupDto setTags(List<TagDto> tags) {
    this.tags = tags;
    return this;
  }

  public Integer getParentId() {
    return parentId;
  }

  public GroupDto setParentId(Integer parentId) {
    this.parentId = parentId;
    return this;
  }

  public Boolean getIsRootGrp() {
    return isRootGrp;
  }

  public GroupDto setIsRootGrp(Boolean isRootGrp) {
    this.isRootGrp = isRootGrp;
    return this;
  }
}
