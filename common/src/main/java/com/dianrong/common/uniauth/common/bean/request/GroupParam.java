package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

public class GroupParam extends Operator {
	private Integer id;
	private String code;
	private String name;
	private String description;
	private Byte status;

	//when add
	private Integer targetGroupId;
	//if true only group, ignore members under each group
	//if false, both group and members returned
	private boolean onlyShowGroup;
	
	private Integer roleId;
	
	private Integer domainId;
	
	private List<RoleDto> roleList;

	public Integer getId() {
		return id;
	}

	public GroupParam setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getCode() {
		return code;
	}

	public GroupParam setCode(String code) {
		this.code = code;
		return this;
	}

	public String getName() {
		return name;
	}

	public GroupParam setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public GroupParam setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public GroupParam setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Integer getTargetGroupId() {
		return targetGroupId;
	}

	public GroupParam setTargetGroupId(Integer targetGroupId) {
		this.targetGroupId = targetGroupId;
		return this;
	}

	public boolean isOnlyShowGroup() {
		return onlyShowGroup;
	}

	public GroupParam setOnlyShowGroup(boolean onlyShowGroup) {
		this.onlyShowGroup = onlyShowGroup;
		return this;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public GroupParam setRoleId(Integer roleId) {
		this.roleId = roleId;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public GroupParam setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public GroupParam setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
		return this;
	}
}
