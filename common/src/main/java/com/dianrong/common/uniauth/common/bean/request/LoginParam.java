package com.dianrong.common.uniauth.common.bean.request;

public class LoginParam extends Operator {
	//domainCode may does not need involve in login process
	private String domainCode;
	private String account;
	//if ture, then fill in auth date;if false, just return user info
	private boolean fillAuthInfo;
	
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public boolean isFillAuthInfo() {
		return fillAuthInfo;
	}
	public void setFillAuthInfo(boolean fillAuthInfo) {
		this.fillAuthInfo = fillAuthInfo;
	}
}
