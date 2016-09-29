package com.dianrong.common.uniauth.common.bean.request;

import java.io.Serializable;

import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Arc on 14/1/16.
 */
public class Operator implements Serializable {
	private static final long serialVersionUID = 2506508045350825576L;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long opUserId;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer opDomainId;
	
	// 租户id
	protected Integer tenancyId;
	
	// 租户Code
	protected String tenancyCode;
	
	public Operator() {
		opUserId = ReflectionUtils.getOpUserId();
		opDomainId = (Integer)ReflectionUtils.getStaticField(DomainDefine.class.getName(), "domainId");
	}

	public Long getOpUserId() {
		return opUserId;
	}
	
	public Integer getOpDomainId(){
		return opDomainId;
	}

	public Integer getTenancyId() {
		return tenancyId;
	}

	public void setTenancyId(Integer tenancyId) {
		this.tenancyId = tenancyId;
	}
	
	public String getTenancyCode() {
		return tenancyCode;
	}

	public void setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
	}
}
