package com.dianrong.common.uniauth.common.bean.request;

/**
 * Created by Arc on 11/4/2016.
 */
public class TagTypeParam extends Operator {

  private static final long serialVersionUID = 5524584740041272751L;
  private Integer id;
  private String code;
  private Integer domainId;

  public Integer getId() {
    return id;
  }

  public TagTypeParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getCode() {
    return code;
  }

  public TagTypeParam setCode(String code) {
    this.code = code;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public TagTypeParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  @Override
  public String toString() {
    return "TagTypeParam [id=" + id + ", code=" + code + ", domainId=" + domainId + "]";
  }
}
