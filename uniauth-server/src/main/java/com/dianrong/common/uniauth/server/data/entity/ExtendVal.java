package com.dianrong.common.uniauth.server.data.entity;

public class ExtendVal {

  private Long id;
  
  private Long extendId;

  private String value;

  public Long getId() {
    return id;
  }

  public ExtendVal setId(Long id) {
    this.id = id;
    return this;
  }

  public Long getExtendId() {
    return extendId;
  }

  public ExtendVal setExtendId(Long extendId) {
    this.extendId = extendId;
    return this;
  }

  public String getValue() {
    return value;
  }

  public ExtendVal setValue(String value) {
    this.value = value;
    return this;
  }
}
