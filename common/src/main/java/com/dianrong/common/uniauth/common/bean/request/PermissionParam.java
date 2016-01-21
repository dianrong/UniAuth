package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.dto.RoleDto;

public class PermissionParam {
	private Integer id;
	private String value;
	private String description;
	private Byte status;
	private Integer permTypeId;
	private Integer domainId;
	
	private List<RoleDto> roleList;
	
	public List<RoleDto> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<RoleDto> roleList) {
		this.roleList = roleList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
	public Integer getPermTypeId() {
		return permTypeId;
	}
	public void setPermTypeId(Integer permTypeId) {
		this.permTypeId = permTypeId;
	}
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
}
