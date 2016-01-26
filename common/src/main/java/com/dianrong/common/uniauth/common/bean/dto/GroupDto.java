package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;
import java.util.List;

public class GroupDto {
	private Integer id;
	private String code;
	private String name;
	private Date createDate;
	private Date lastUpdate;
	private Byte status;
	private String description;

	/**
	private List<UserDto> ownerList;
	**/
	private List<UserDto> userList;
	private List<GroupDto> groupList;
	
	//whether this group connected with a role
	private Boolean roleChecked;

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

	public List<UserDto> getUserList() {
		return userList;
	}

	public GroupDto setUserList(List<UserDto> userList) {
		this.userList = userList;
		return this;
	}

	public List<GroupDto> getGroupList() {
		return groupList;
	}

	public GroupDto setGroupList(List<GroupDto> groupList) {
		this.groupList = groupList;
		return this;
	}

	public Boolean getRoleChecked() {
		return roleChecked;
	}

	public GroupDto setRoleChecked(Boolean roleChecked) {
		this.roleChecked = roleChecked;
		return this;
	}
}
