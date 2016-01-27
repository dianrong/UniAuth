package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

public class PermissionParam extends Operator {
	private Integer id;
	private String value;
	private String description;
	private Byte status;
	private Integer permTypeId;
	
	private Integer domainId;
	
	private List<RoleDto> roleList;

	public Integer getId() {
		return id;
	}

	public PermissionParam setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getValue() {
		return value;
	}

	public PermissionParam setValue(String value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public PermissionParam setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public PermissionParam setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Integer getPermTypeId() {
		return permTypeId;
	}

	public PermissionParam setPermTypeId(Integer permTypeId) {
		this.permTypeId = permTypeId;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public PermissionParam setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public List<RoleDto> getRoleList() {
		return roleList;
	}

	public PermissionParam setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
		return this;
	}
}
