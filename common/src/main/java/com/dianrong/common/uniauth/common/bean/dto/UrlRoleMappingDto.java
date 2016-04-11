package com.dianrong.common.uniauth.common.bean.dto;

public class UrlRoleMappingDto {

	private String permUrl;
	private String roleCode;
	private String permType;
	private String httpMethod;
	
	public String getPermUrl() {
		return permUrl;
	}
	public void setPermUrl(String permUrl) {
		this.permUrl = permUrl;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getPermType() {
		return permType;
	}
	public void setPermType(String permType) {
		this.permType = permType;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

}
