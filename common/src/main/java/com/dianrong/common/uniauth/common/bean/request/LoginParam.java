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
	
	// 租户id
	private  Integer tenancyId;
	
	private String tenancyCode;
	
	public Integer getTenancyId() {
		return tenancyId;
	}

	public void setTenancyId(Integer tenancyId) {
		this.tenancyId = tenancyId;
	}

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

	public String getTenancyCode() {
		return tenancyCode;
	}

	public void setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
	}
}
