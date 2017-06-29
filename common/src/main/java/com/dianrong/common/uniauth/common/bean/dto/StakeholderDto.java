package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

import lombok.ToString;

@ToString
public class StakeholderDto implements Serializable {

  private static final long serialVersionUID = 2120671490345809592L;
  private Integer id;
  private String name;
  private String email;
  private String phone;
  private String jobtitle;
  private Integer domainId;

  public Integer getId() {
    return id;
  }

  public StakeholderDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public StakeholderDto setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public StakeholderDto setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public StakeholderDto setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getJobtitle() {
    return jobtitle;
  }

  public StakeholderDto setJobtitle(String jobtitle) {
    this.jobtitle = jobtitle;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public StakeholderDto setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }
}
