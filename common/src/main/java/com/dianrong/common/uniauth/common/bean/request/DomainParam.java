package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

public class DomainParam extends PageParam {
	private List<Integer> domainIds;
    private Integer id;
    //generally, code cann't be changed!
    private String code;
    private String displayName;
    private String description;
    private Byte status;
    
    private List<String> domainCodeList;
    
	public List<String> getDomainCodeList() {
		return domainCodeList;
	}
	public void setDomainCodeList(List<String> domainCodeList) {
		this.domainCodeList = domainCodeList;
	}
	public Integer getId() {
		return id;
	}
	public DomainParam setId(Integer id) {
		this.id = id;
		return this;
	}
	public String getCode() {
		return code;
	}
	public DomainParam setCode(String code) {
		this.code = code;
		return this;
	}
	public String getDisplayName() {
		return displayName;
	}
	public DomainParam setDisplayName(String displayName) {
		this.displayName = displayName;
		return this;
	}
	public String getDescription() {
		return description;
	}
	public DomainParam setDescription(String description) {
		this.description = description;
		return this;
	}
	public Byte getStatus() {
		return status;
	}
	public DomainParam setStatus(Byte status) {
		this.status = status;
		return this;
	}

	public List<Integer> getDomainIds() {
		return domainIds;
	}

	public DomainParam setDomainIds(List<Integer> domainIds) {
		this.domainIds = domainIds;
		return this;
	}
}
