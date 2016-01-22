package com.dianrong.common.uniauth.common.bean.request;

public class PageParam extends Operator {
    //from index 0
    protected Integer pageOffset;
    protected Integer pageSize;
    
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
