package com.dianrong.common.uniauth.common.bean.request;

public class GroupParam extends Operator {
	private Integer id;
	private String code;
	private String name;
	private String description;
	
	//when add
	private Integer targetGroupId;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTargetGroupId() {
		return targetGroupId;
	}

	public void setTargetGroupId(Integer targetGroupId) {
		this.targetGroupId = targetGroupId;
	}

}
