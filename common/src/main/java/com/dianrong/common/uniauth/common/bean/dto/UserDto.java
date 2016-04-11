package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Arc on 13/1/16.
 */
//@XmlRootElement
public class UserDto {
	private Long id;
	private String name;
	private String password;
	private String passwordSalt;
	private String email;
	private String phone;
	private Date createDate;
	private Date lastUpdate;
	private Byte status;
	private Byte userGroupType;
	private List<TagDto> tagDtos;

	//whether this user directly connected with a role
	private Boolean roleChecked;

	public Long getId() {
		return id;
	}

	public UserDto setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public UserDto setName(String name) {
		this.name = name;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserDto setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public UserDto setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserDto setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public UserDto setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public Boolean getRoleChecked() {
		return roleChecked;
	}

	public UserDto setRoleChecked(Boolean roleChecked) {
		this.roleChecked = roleChecked;
		return this;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public UserDto setCreateDate(Date createDate) {
		this.createDate = createDate;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public UserDto setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Byte getUserGroupType() {
		return userGroupType;
	}

	public UserDto setUserGroupType(Byte userGroupType) {
		this.userGroupType = userGroupType;
		return this;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public UserDto setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
		return this;
	}

	public List<TagDto> getTagDtos() {
		return tagDtos;
	}

	public UserDto setTagDtos(List<TagDto> tagDtos) {
		this.tagDtos = tagDtos;
		return this;
	}
}
