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
}
