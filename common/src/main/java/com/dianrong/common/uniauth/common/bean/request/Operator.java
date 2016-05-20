package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Arc on 14/1/16.
 */
public class Operator implements Serializable {
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long opUserId;

	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Integer opDomainId;
	
	public Operator() {
		opUserId = ReflectionUtils.getOpUserId();
		opDomainId = (Integer)ReflectionUtils.getStaticField("com.dianrong.common.uniauth.common.client.DomainDefine", "domainId");
	}

	public Long getOpUserId() {
		return opUserId;
	}
	
	public Integer getOpDomainId(){
		return opDomainId;
	}
}
