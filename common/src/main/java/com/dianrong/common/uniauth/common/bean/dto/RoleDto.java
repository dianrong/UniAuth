package com.dianrong.common.uniauth.common.bean.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by Arc on 14/1/16.
 */
public class RoleDto {

	private Integer id;
	private String code;
	private String name;
	
	private List<GroupDto> groupList;
	private List<Map<String, List<String>>> permissionList;
	
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
}
