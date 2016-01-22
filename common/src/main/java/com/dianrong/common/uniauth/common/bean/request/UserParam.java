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
    
    private UserActionEnum userActionEnum;
    
    private Integer domainId;
    
    private List<RoleDto> roleList;
    
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserActionEnum getUserActionEnum() {
		return userActionEnum;
	}

	public void setUserActionEnum(UserActionEnum userActionEnum) {
		this.userActionEnum = userActionEnum;
	}
}
