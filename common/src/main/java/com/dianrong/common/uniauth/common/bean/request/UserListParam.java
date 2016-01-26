package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class UserListParam extends Operator{
	private List<Long> userIds;
	//target group id
	private Integer groupId;
	
	//true normal member, false owner member
	private Boolean normalMember;

	public List<Long> getUserIds() {
		return userIds;
	}

	public UserListParam setUserIds(List<Long> userIds) {
		this.userIds = userIds;
		return this;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public UserListParam setGroupId(Integer groupId) {
		this.groupId = groupId;
		return this;
	}

	public Boolean getNormalMember() {
		return normalMember;
	}

	public UserListParam setNormalMember(Boolean normalMember) {
		this.normalMember = normalMember;
		return this;
	}
}
