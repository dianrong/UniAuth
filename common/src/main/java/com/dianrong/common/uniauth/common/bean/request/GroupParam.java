package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

public class GroupParam extends Operator {
	private Integer id;
	private String code;
	private String name;
	private String description;
	
	//when add
	private Integer targetGroupId;
	//if true only group, ignore members under each group
	//if false, both group and members returned
	private boolean onlyShowGroup;
	
	private Integer roleId;
	
	private Integer domainId;
	
	private List<RoleDto> roleList;

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

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTargetGroupId() {
		return targetGroupId;
	}

	public void setTargetGroupId(Integer targetGroupId) {
		this.targetGroupId = targetGroupId;
	}

	public boolean isOnlyShowGroup() {
		return onlyShowGroup;
	}

	public void setOnlyShowGroup(boolean onlyShowGroup) {
		this.onlyShowGroup = onlyShowGroup;
	}
}
