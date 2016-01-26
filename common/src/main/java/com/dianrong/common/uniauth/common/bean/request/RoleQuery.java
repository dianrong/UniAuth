package com.dianrong.common.uniauth.common.bean.request;

public class RoleQuery extends PageParam {
	private Integer id;
	private String name;
	private String description;
	private Byte status;
	private Integer roleCodeId;
	
	private Integer domainId;

	public Integer getId() {
		return id;
	}

	public RoleQuery setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public RoleQuery setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public RoleQuery setDescription(String description) {
		this.description = description;
		return this;
	}

	public Byte getStatus() {
		return status;
	}

	public RoleQuery setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public Integer getRoleCodeId() {
		return roleCodeId;
	}

	public RoleQuery setRoleCodeId(Integer roleCodeId) {
		this.roleCodeId = roleCodeId;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public RoleQuery setDomainId(Integer domainId) {
		this.domainId = domainId;
		return this;
	}
}
