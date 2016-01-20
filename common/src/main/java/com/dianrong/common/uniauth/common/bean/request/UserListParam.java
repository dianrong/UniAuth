package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class UserListParam extends Operator{
	private List<UserParam> userList;
	//target group id
	private Integer groupId;
	
	public List<UserParam> getUserList() {
		return userList;
	}
	public void setUserList(List<UserParam> userList) {
		this.userList = userList;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
}
