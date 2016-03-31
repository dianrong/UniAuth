package com.dianrong.common.uniauth.cas.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jasig.cas.authentication.UsernamePasswordCredential;

public class CasUsernamePasswordCredential extends UsernamePasswordCredential {
	private static final long serialVersionUID = -5552074507929396707L;

    @NotNull
    @Size(min=1, message = "required.domain")
	private String domain;
    
    /**.
     * 验证码
     */
	private String captcha;
	
	public CasUsernamePasswordCredential() {
	}
	
	public CasUsernamePasswordCredential(String userName, String password) {
		super(userName, password);
	}

	public CasUsernamePasswordCredential(String userName, String password, String domain) {
		this(userName, password, domain, "");
	}
	
	public CasUsernamePasswordCredential(String userName, String password, String domain, String captcha) {
		super(userName, password);
		this.domain = domain;
		this.captcha = captcha;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, false);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this,obj, false);
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
