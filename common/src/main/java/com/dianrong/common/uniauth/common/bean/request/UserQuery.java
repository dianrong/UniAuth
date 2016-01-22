package com.dianrong.common.uniauth.common.bean.request;

/**
 * Created by Arc on 15/1/16.
 */
public class UserQuery extends PageParam {

    private String name;
    private String phone;
    private String email;

    public String getName() {
        return name;
    }

    public UserQuery setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public UserQuery setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserQuery setEmail(String email) {
        this.email = email;
        return this;
    }

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
