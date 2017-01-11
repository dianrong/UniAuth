package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;
import java.util.List;

public class GroupDto extends TenancyBaseDto {
	private static final long serialVersionUID = 892184382591361189L;
	private Integer id;
	private String code;
	private String name;
	private Date createDate;
	private Date lastUpdate;
	private Byte status;
	private String description;
	private List<TagDto> tags;
	private Integer parentId;
	/**
	private List<UserDto> ownerList;
	**/
	private List<UserDto> users;
	private List<GroupDto> groups;
	
	//whether this group connected with a role
	private Boolean roleChecked;
	//whether this group connected with a tag
	private Boolean tagChecked;
	private Boolean ownerMarkup;
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

	@Override
	public String toString() {
		return "GroupDto [id=" + id + ", code=" + code + ", name=" + name + ", createDate=" + createDate
				+ ", lastUpdate=" + lastUpdate + ", status=" + status + ", description=" + description + ", tags="
				+ tags + ", parentId=" + parentId + ", users=" + users + ", groups=" + groups + ", roleChecked="
				+ roleChecked + ", tagChecked=" + tagChecked + ", ownerMarkup=" + ownerMarkup + ", isRootGrp="
				+ isRootGrp + "]";
	}
}
