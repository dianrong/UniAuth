package com.dianrong.common.uniauth.common.bean.request;

public class LoginParam extends Operator {
	private static final long serialVersionUID = 7180777080519450378L;
	//domainCode may does not need involve in login process
	/**
	private String domainCode;
	*/
	private String account;
	//only apply to cas sso
	private String password;
	private String ip;

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

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public LoginParam setAccount(String account) {
		this.account = account;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public LoginParam setPassword(String password) {
		this.password = password;
		return this;
	}

	@Override
	public String toString() {
		return "LoginParam [account=" + account + ", password=" + password + ", ip=" + ip + "]";
	}
}
