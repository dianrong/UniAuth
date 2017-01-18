package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.List;

public class PageDto<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 578665277377363989L;
	private int currentPage;       // current page number, from 0
    private int pageSize;   // page size
    private int totalCount; // total record count
    private List<T> data;   // current page data

    public PageDto() {
        super();
    }

    public PageDto(int currentPage, int pageSize, int totalCount, List<T> data) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;
    }

    public int getTotalPage() {
        if ((pageSize <= 0) || (totalCount <= 0)) {
            return 0;
        }

        if (totalCount % pageSize == 0) {
            return totalCount / pageSize;
        } else {
            return totalCount / pageSize + 1;
        }
    }

    public PageDto<T> setTotalPage(int totalPage) {
        return this;
    }

    public boolean hasPreviousPage() {
        return (currentPage > 0) && (currentPage + 1 <= getTotalPage());
    }

    public boolean hasNextPage() {
        return (currentPage >= 0) && (currentPage + 1 < getTotalPage());
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public PageDto<T> setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageDto<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public PageDto<T> setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public List<T> getData() {
        return data;
    }

    public PageDto<T> setData(List<T> data) {
        this.data = data;
        return this;
    }

	@Override
	public String toString() {
		return "PageDto [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalCount=" + totalCount
				+ ", data=" + data + "]";
	}
}
