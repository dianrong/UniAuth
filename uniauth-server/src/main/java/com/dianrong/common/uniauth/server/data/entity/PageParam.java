package com.dianrong.common.uniauth.server.data.entity;

/**
 * . 分页相关的字段定义
 *
 * @author wanglin
 */
public class PageParam {

  private int pageSize;

  private int pageOffSet;

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getPageOffSet() {
    return pageOffSet;
  }

  public void setPageOffSet(int pageOffSet) {
    this.pageOffSet = pageOffSet;
  }
}
