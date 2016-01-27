package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.enm.RoleActionEnum;

public class RoleParam extends Operator {
	private Integer id;
	private String name;
	private String description;
	private Byte status;
	private Integer roleCodeId;
	private Integer domainId;
	private RoleActionEnum roleActionEnum;
	private List<PermissionDto> permsList;

	public Integer getId() {
		return id;
	}

	public RoleParam setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public RoleParam setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RoleParam setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public RoleParam setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Integer getRoleCodeId() {
		return roleCodeId;
	}

	public RoleParam setRoleCodeId(Integer roleCodeId) {
		this.roleCodeId = roleCodeId;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public RoleParam setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public List<PermissionDto> getPermsList() {
		return permsList;
	}

	public RoleParam setPermsList(List<PermissionDto> permsList) {
		this.permsList = permsList;
		return this;
	}

	public RoleActionEnum getRoleActionEnum() {
		return roleActionEnum;
	}

	public RoleParam setRoleActionEnum(RoleActionEnum roleActionEnum) {
		this.roleActionEnum = roleActionEnum;
		return this;
	}
}
