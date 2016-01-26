package com.dianrong.common.uniauth.common.bean.request;

public class PageParam extends Operator {
    //from index 0
    protected Integer pageOffset;
    protected Integer pageSize;

	public Integer getPageOffset() {
		return pageOffset;
	}

	public PageParam setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
		return this;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public PageParam setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		return this;
	}
}
