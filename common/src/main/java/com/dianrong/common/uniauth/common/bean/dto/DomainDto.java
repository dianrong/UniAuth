package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;

public class DomainDto {
	private Integer id;
	private String code;
	private String displayName;
	private String description;
	private Byte status;
	

	private List<RoleDto> roleList;
	private List<StakeholderDto> stakeholderList;
	
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
	
	public List<StakeholderDto> getStakeholderList() {
		return stakeholderList;
	}

	public void setStakeholderList(List<StakeholderDto> stakeholderList) {
		this.stakeholderList = stakeholderList;
	}

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
