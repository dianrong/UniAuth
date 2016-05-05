package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

public class PermTypeDto implements Serializable {
	private Integer id;
	private String type;

	public Integer getId() {
		return id;
	}

	public PermTypeDto setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getType() {
		return type;
	}

	public PermTypeDto setType(String type) {
		this.type = type;
		return this;
	}
}
