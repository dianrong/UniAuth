package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class RoleQuery extends PageParam {
	private static final long serialVersionUID = -2330120093448174753L;
	private Integer id;
	private String name;
	private String description;
	private Byte status;
	private Integer roleCodeId;
	private List<Integer> roleIds;
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

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public RoleQuery setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
		return this;
	}

	@Override
	public String toString() {
		return "RoleQuery [id=" + id + ", name=" + name + ", description=" + description + ", status=" + status
				+ ", roleCodeId=" + roleCodeId + ", roleIds=" + roleIds + ", domainId=" + domainId + "]";
	}
}
