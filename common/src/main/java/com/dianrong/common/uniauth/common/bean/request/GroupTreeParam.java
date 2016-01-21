package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.bean.dto.GroupDto;

public class GroupTreeParam extends Operator {
	private GroupDto groupDto;
	private Integer roleId;
	
	public GroupDto getGroupDto() {
		return groupDto;
	}
	public void setGroupDto(GroupDto groupDto) {
		this.groupDto = groupDto;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
