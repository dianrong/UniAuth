package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.enm.UserActionEnum;

public class UserParam extends Operator {
	private Long id;
    private String name;
    private String phone;
    private String email;
    private String password;
    private Byte status;

    private UserActionEnum userActionEnum;
    
    private Integer domainId;
    
    private List<RoleDto> roleList;

	public Long getId() {
		return id;
	}

	public UserParam setId(Long id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public UserParam setName(String name) {
		this.name = name;
		return this;
	}

	public String getPhone() {
		return phone;
	}

	public UserParam setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public UserParam setEmail(String email) {
		this.email = email;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public UserParam setPassword(String password) {
		this.password = password;
		return this;
	}

	public UserActionEnum getUserActionEnum() {
		return userActionEnum;
	}

	public UserParam setUserActionEnum(UserActionEnum userActionEnum) {
		this.userActionEnum = userActionEnum;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public UserParam setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public UserParam setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public UserParam setStatus(Byte status) {
		this.status = status;
		return this;
	}
}
