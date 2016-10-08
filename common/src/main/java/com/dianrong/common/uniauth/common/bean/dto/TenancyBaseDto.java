package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

public class TenancyBaseDto  implements Serializable{

	private static final long serialVersionUID = 7612408066715418041L;
	
	// 租户id
	private Integer tenancyId;
	
	// 租户Code
	private String tenancyCode;

	public Integer getTenancyId() {
		return tenancyId;
	}

	public TenancyBaseDto setTenancyId(Integer tenancyId) {
		this.tenancyId = tenancyId;
		return this;
	}

	public String getTenancyCode() {
		return tenancyCode;
	}

	public TenancyBaseDto setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
		return this;
	}
}
