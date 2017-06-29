package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.ToString;

@ToString
public class ApiCallerInfoDto implements Serializable {

  private static final long serialVersionUID = -7245175906463018288L;

  private Integer id;
  private Integer domainId;
  private String domainCode;
  private String domainName;
  private String password;
  private Byte status;
  private Date createDate;
  private Date lastUpdate;

  public Integer getId() {
    return id;
  }

  public ApiCallerInfoDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public ApiCallerInfoDto setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public ApiCallerInfoDto setPassword(String password) {
    this.password = password;
    return this;
  }

  public Byte getStatus() {
    return status;
  }

  public ApiCallerInfoDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public ApiCallerInfoDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public ApiCallerInfoDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }

  public String getDomainCode() {
    return domainCode;
  }

  public ApiCallerInfoDto setDomainCode(String domainCode) {
    this.domainCode = domainCode;
    return this;
  }

  public String getDomainName() {
    return domainName;
  }

  public ApiCallerInfoDto setDomainName(String domainName) {
    this.domainName = domainName;
    return this;
  }
}
