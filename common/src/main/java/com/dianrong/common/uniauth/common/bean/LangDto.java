package com.dianrong.common.uniauth.common.bean;

public class LangDto {
	
	
	public LangDto(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	private String code;
	
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
