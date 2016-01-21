package com.dianrong.common.uniauth.common.bean.request;

public class LoginParam extends Operator {
	//domainCode may does not need involve in login process
	/**
	private String domainCode;
	*/
	private String account;
	//only apply to cas sso
	private String password;

	/**
	public String getDomainCode() {
		return domainCode;
	}
	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}
	**/
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
