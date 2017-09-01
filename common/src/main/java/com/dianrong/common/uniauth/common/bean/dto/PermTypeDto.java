package com.dianrong.common.uniauth.common.bean.dto;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class PermTypeDto implements Serializable {

  private static final long serialVersionUID = 4831307200565666797L;
  private Integer id;
  private String type;

  public Integer getId() {
    return id;
  }

  public PermTypeDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getType() {
    return type;
  }

  public PermTypeDto setType(String type) {
    this.type = type;
    return this;
  }
}
