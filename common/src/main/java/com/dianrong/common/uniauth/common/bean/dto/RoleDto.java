package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by Arc on 14/1/16.
 */
public class RoleDto {
	private Integer id;
	private String name;
	private String description;
	private Byte status;
	private Integer domainId;
	private Integer roleCodeId;
	private RoleCodeDto roleCodeDto;
	
	private List<GroupDto> groupList;
	private List<Map<String, List<String>>> permissionList;
	
	//whether this role connected with this permission
	//private boolean permChecked;
	//whether this role connected with this user
	//private boolean roleChecked;
	//whether this role connected with this group
	//private boolean groupChecked;
	
	//whether this role connected with a permission, user or group
	private boolean checked;
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
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
	public List<GroupDto> getGroupList() {
		return groupList;
	}
	public void setGroupList(List<GroupDto> groupList) {
		this.groupList = groupList;
	}
	public List<Map<String, List<String>>> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Map<String, List<String>>> permissionList) {
		this.permissionList = permissionList;
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
	public RoleCodeDto getRoleCodeDto() {
		return roleCodeDto;
	}
	public void setRoleCodeDto(RoleCodeDto roleCodeDto) {
		this.roleCodeDto = roleCodeDto;
	}
}
