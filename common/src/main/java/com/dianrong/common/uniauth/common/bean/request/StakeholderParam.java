package com.dianrong.common.uniauth.common.bean.request;

public class StakeholderParam extends Operator {

  private static final long serialVersionUID = 7155432975823370632L;
  private Integer id;
  private String name;
  private String email;
  private String phone;
  private String jobtitle;
  private Integer domainId;

  public Integer getId() {
    return id;
  }

  public StakeholderParam setId(Integer id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public StakeholderParam setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public StakeholderParam setEmail(String email) {
    this.email = email;
    return this;
  }

  public String getPhone() {
    return phone;
  }

  public StakeholderParam setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public String getJobtitle() {
    return jobtitle;
  }

  public StakeholderParam setJobtitle(String jobtitle) {
    this.jobtitle = jobtitle;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public StakeholderParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }
}
