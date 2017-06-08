package com.dianrong.common.uniauth.common.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel("分页查询返回结果")
public class PageDto<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = 578665277377363989L;

  @ApiModelProperty(value = "当前页码", required = true)
  private int currentPage; // current page number, from 0
  @ApiModelProperty(value = "每页数量大小", required = true)
  private int pageSize; // page size
  @ApiModelProperty(value = "数据总条数", required = true)
  private int totalCount; // total record count
  @ApiModelProperty(value = "查询结果数据", required = true)
  private List<T> data; // current page data

  public PageDto() {
    super();
  }

  /**
   * 构造函数.
   */
  public PageDto(int currentPage, int pageSize, int totalCount, List<T> data) {
    this.currentPage = currentPage;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
    this.data = data;
  }

  /**
   * 返回总页数.
   */
  @ApiModelProperty(value = "总页数", required = true)
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
    return "PageDto [currentPage=" + currentPage + ", pageSize=" + pageSize + ", totalCount="
        + totalCount + ", data=" + data + "]";
  }
}
