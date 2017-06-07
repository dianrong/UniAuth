package com.dianrong.common.uniauth.common.bean.request;

import java.util.List;

/**
 * Created by Arc on 8/4/2016.
 */
public class TagTypeQuery extends Operator {

  private static final long serialVersionUID = -3924943487606336730L;
  private Integer id;
  private List<Integer> domainIds;
  private Integer domainId;
  private String code;

  public Integer getId() {
    return id;
  }

  public TagTypeQuery setId(Integer id) {
    this.id = id;
    return this;
  }

  public List<Integer> getDomainIds() {
    return domainIds;
  }

  public TagTypeQuery setDomainIds(List<Integer> domainIds) {
    this.domainIds = domainIds;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public TagTypeQuery setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public String getCode() {
    return code;
  }

  public TagTypeQuery setCode(String code) {
    this.code = code;
    return this;
  }

  @Override
  public String toString() {
    return "TagTypeQuery [id=" + id + ", domainIds=" + domainIds + ", domainId=" + domainId
        + ", code=" + code + "]";
  }
}
