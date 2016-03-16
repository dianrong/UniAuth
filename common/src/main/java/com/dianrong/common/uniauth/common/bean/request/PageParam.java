package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.cons.AppConstants;

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
		if(pageSize != null && pageSize > AppConstants.MAX_PAGE_SIZE) {
			throw new RuntimeException("pageSize can not exceed 5000, if over 5000, please enable the pagination function");
		}
		return pageSize;
	}

	public PageParam setPageSize(Integer pageSize) {
		if(pageSize != null && pageSize > AppConstants.MAX_PAGE_SIZE) {
			throw new RuntimeException("pageSize can not exceed 5000, if over 5000, please enable the pagination function");
		}
		this.pageSize = pageSize;
		return this;
	}
}
