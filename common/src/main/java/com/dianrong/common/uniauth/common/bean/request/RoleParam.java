package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;

public class RoleParam {
	private Integer id;
	private String name;
	private String description;
	private Byte status;
	private Integer domainId;
	private Integer roleCodeId;
	
	private List<PermissionDto> permsList;
	
	public List<PermissionDto> getPermsList() {
		return permsList;
	}
	public void setPermsList(List<PermissionDto> permsList) {
		this.permsList = permsList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public Integer getRoleCodeId() {
		return roleCodeId;
	}
	public void setRoleCodeId(Integer roleCodeId) {
		this.roleCodeId = roleCodeId;
	}
}
