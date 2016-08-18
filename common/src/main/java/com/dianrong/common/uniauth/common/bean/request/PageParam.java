package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.cons.AppConstants;

public class PageParam extends Operator {
	private static final long serialVersionUID = -816255290618366452L;
	// from index 0, default value 0
    protected Integer pageNumber = AppConstants.MIN_PAGE_NUMBER;
	// default 5000.
	// if api user use defaultValue/5000 and when the query result number exceed 5000 the program will throw an exception to say that
	// you must customize your params for pagination requirement.
    protected Integer pageSize = AppConstants.MAX_PAGE_SIZE;

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

	@Override
	public String toString() {
		return "PageParam [pageNumber=" + pageNumber + ", pageSize=" + pageSize + "]";
	}
}
