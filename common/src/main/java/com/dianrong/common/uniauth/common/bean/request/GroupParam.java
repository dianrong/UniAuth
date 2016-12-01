package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class GroupParam extends Operator {

	private static final long serialVersionUID = -4261321484001881493L;
	private Integer id;
	private String code;
	private String name;
	private String description;
	private Byte status;

	//when add
	private Integer targetGroupId;
	//when delete
	private List<Integer> targetGroupIds;
	//if true only group, ignore members under each group
	//if false, both group and members returned
	private Boolean onlyShowGroup;
	//if onlyShowGroup=false and userGroupType=0, then return members, if if onlyShowGroup=false and userGroupType=1 return owners
	private Byte userGroupType;

	private Boolean needOwnerMarkup;

	private Integer roleId;

	private Integer tagId;
	
	private Integer domainId;
	
	private List<Integer> roleIds;
	
	private List<Integer> tagIds;

	private Boolean needUserRelationShip;
	
	private Boolean needAllGrp;
	
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

	public Boolean getNeedUserRelationShip() {
		return needUserRelationShip;
	}
	
	public GroupParam setNeedUserRelationShip(Boolean needUserRelationShip) {
		this.needUserRelationShip = needUserRelationShip;
		return this;
	}

	@Override
	public String toString() {
		return "GroupParam [id=" + id + ", code=" + code + ", name=" + name + ", description=" + description
				+ ", status=" + status + ", targetGroupId=" + targetGroupId + ", targetGroupIds=" + targetGroupIds
				+ ", onlyShowGroup=" + onlyShowGroup + ", userGroupType=" + userGroupType + ", needOwnerMarkup="
				+ needOwnerMarkup + ", roleId=" + roleId + ", tagId=" + tagId + ", domainId=" + domainId + ", roleIds="
				+ roleIds + ", tagIds=" + tagIds + ", needUserRelationShip=" + needUserRelationShip + ", opUserId="
				+ opUserId + ", opDomainId=" + opDomainId + ", getId()=" + getId() + ", getCode()=" + getCode()
				+ ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getStatus()=" + getStatus()
				+ ", getTargetGroupId()=" + getTargetGroupId() + ", getOnlyShowGroup()=" + getOnlyShowGroup()
				+ ", getRoleId()=" + getRoleId() + ", getDomainId()=" + getDomainId() + ", getRoleIds()=" + getRoleIds()
				+ ", getUserGroupType()=" + getUserGroupType() + ", getNeedOwnerMarkup()=" + getNeedOwnerMarkup()
				+ ", getTargetGroupIds()=" + getTargetGroupIds() + ", getTagId()=" + getTagId() + ", getTagIds()="
				+ getTagIds() + ", getNeedUserRelationShip()=" + getNeedUserRelationShip() + ", getOpUserId()="
				+ getOpUserId() + ", getOpDomainId()=" + getOpDomainId() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}

	public Boolean getNeedAllGrp() {
		return needAllGrp;
	}

	public GroupParam setNeedAllGrp(Boolean needAllGrp) {
		this.needAllGrp = needAllGrp;
		return this;
	}
}
