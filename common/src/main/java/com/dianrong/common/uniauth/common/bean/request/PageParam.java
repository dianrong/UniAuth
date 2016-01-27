package com.dianrong.common.uniauth.common.bean.request;

public class PageParam extends Operator {
    //from index 0
    protected Integer pageNumber;
    protected Integer pageSize;

	public Integer getPageNumber() {
		return pageNumber;
	}

	public PageParam setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
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
