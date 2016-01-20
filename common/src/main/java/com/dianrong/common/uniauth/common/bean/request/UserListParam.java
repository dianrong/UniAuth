package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class UserListParam extends Operator{
	private List<UserParam> userList;
	//target group id
	private Integer groupId;
	
	//true normal member, false owner member
	private boolean normalMember;
	
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
	public boolean isNormalMember() {
		return normalMember;
	}
	public void setNormalMember(boolean normalMember) {
		this.normalMember = normalMember;
	}
}
