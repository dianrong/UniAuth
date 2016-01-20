package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;

public class GroupDto {
	private Integer id;
	private String code;
	private String name;
	
	/**
	private List<UserDto> ownerList;
	**/
	private List<UserDto> userList;
	private List<GroupDto> groupList;

	/**
	public List<UserDto> getOwnerList() {
		return ownerList;
	}

	public void setOwnerList(List<UserDto> ownerList) {
		this.ownerList = ownerList;
	}
	**/

	public List<GroupDto> getGroupList() {
		return groupList;
	}

	public void setGroupList(List<GroupDto> groupList) {
		this.groupList = groupList;
	}

	public List<UserDto> getUserList() {
		return userList;
	}

	public void setUserList(List<UserDto> userList) {
		this.userList = userList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
