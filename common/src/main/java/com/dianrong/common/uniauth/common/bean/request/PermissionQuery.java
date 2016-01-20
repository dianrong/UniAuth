package com.dianrong.common.uniauth.common.bean.request;

public class PermissionQuery {
	private Integer id;
	private String value;
	private String description;
	private Byte status;
	private Integer permTypeId;
	private Integer domainId;
	
    //from index 0
    private Integer pageOffset;
    private Integer pageSize;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public Integer getPermTypeId() {
		return permTypeId;
	}
	public void setPermTypeId(Integer permTypeId) {
		this.permTypeId = permTypeId;
	}
	public Integer getDomainId() {
		return domainId;
	}
	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}
	public Integer getPageOffset() {
		return pageOffset;
	}
	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
}
