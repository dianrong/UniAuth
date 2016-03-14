package com.dianrong.common.uniauth.common.client;

public class DomainDefine {
	private String domainCode;
	private String userInfoClass;
	
	private static Integer domainId;

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getUserInfoClass() {
		return userInfoClass;
	}

	public void setUserInfoClass(String userInfoClass) {
		this.userInfoClass = userInfoClass;
	}
}
