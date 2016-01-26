package com.dianrong.common.uniauth.common.bean.dto;

public class PermissionDto {
	private Integer id;
	private String value;
	private String description;
	private Byte status;
	private Integer permTypeId;
	private Integer domainId;
	
	private PermTypeDto permTypeDto;
	
	//whether this permission connected with this role
	private Boolean checked;

	public Integer getId() {
		return id;
	}

	public PermissionDto setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getValue() {
		return value;
	}

	public PermissionDto setValue(String value) {
		this.value = value;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public PermissionDto setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public PermissionDto setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Integer getPermTypeId() {
		return permTypeId;
	}

	public PermissionDto setPermTypeId(Integer permTypeId) {
		this.permTypeId = permTypeId;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public PermissionDto setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}

	public PermTypeDto getPermTypeDto() {
		return permTypeDto;
	}

	public PermissionDto setPermTypeDto(PermTypeDto permTypeDto) {
		this.permTypeDto = permTypeDto;
		return this;
	}

	public Boolean getChecked() {
		return checked;
	}

	public PermissionDto setChecked(Boolean checked) {
		this.checked = checked;
		return this;
	}
}
