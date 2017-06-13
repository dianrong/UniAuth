package com.dianrong.common.uniauth.common.bean.request;

public class PrimaryKeyParam extends Operator {

  private static final long serialVersionUID = 2491564431941733779L;
  private Integer id;

  public Integer getId() {
    return id;
  }

  public PrimaryKeyParam setId(Integer id) {
    this.id = id;
    return this;
  }
}
