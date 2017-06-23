package com.dianrong.common.uniauth.server.data.query;

/**
 * Created by Arc on 14/1/16.
 */
public class BaseQueryParam {

  // non-NULL
  private Integer pageOffset;
  private Integer pageSize;

  public Integer getPageOffset() {
    return pageOffset;
  }

  public BaseQueryParam setPageOffset(Integer pageOffset) {
    this.pageOffset = pageOffset;
    return this;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public BaseQueryParam setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }
}
