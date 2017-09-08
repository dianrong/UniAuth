package com.dianrong.common.uniauth.common.bean.request;

import lombok.ToString;

import java.util.Date;

/**
 * 同步操作处理参数.
 */
@ToString
public class HrSynchronousProcessParam extends Operator {
  private static final long serialVersionUID = -7961564170283106319L;
  /**
   * 是否异步执行.
   */
  private Boolean asynchronous;

  public Boolean getAsynchronous() {
    return asynchronous;
  }

  public HrSynchronousProcessParam setAsynchronous(Boolean asynchronous) {
    this.asynchronous = asynchronous;
    return this;
  }
}
