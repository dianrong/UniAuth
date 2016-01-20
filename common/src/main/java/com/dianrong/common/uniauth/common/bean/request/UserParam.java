package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.enm.UserActionEnum;

public class UserParam {
	private Long id;
    private String name;
    private String phone;
    private String email;
    
    private UserActionEnum userActionEnum;

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
