package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.ToString;

@ToString
public class TenancyDto implements Serializable {

  private static final long serialVersionUID = -6480156107431606880L;

  private Long id;

  private String code;

  private String name;

  private String contactName;

  private String phone;

  private String description;

  private Byte status;

  private Date createDate;

  private Date lastUpdate;

  public Long getId() {
    return id;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public String getContactName() {
    return contactName;
  }

  public String getPhone() {
    return phone;
  }

  public String getDescription() {
    return description;
  }

  public Byte getStatus() {
    return status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public Date getLastUpdate() {
    return lastUpdate;
  }

  public TenancyDto setId(Long id) {
    this.id = id;
    return this;
  }

  public TenancyDto setCode(String code) {
    this.code = code;
    return this;
  }

  public TenancyDto setName(String name) {
    this.name = name;
    return this;
  }

  public TenancyDto setContactName(String contactName) {
    this.contactName = contactName;
    return this;
  }

  public TenancyDto setPhone(String phone) {
    this.phone = phone;
    return this;
  }

  public TenancyDto setDescription(String description) {
    this.description = description;
    return this;
  }

  public TenancyDto setStatus(Byte status) {
    this.status = status;
    return this;
  }

  public TenancyDto setCreateDate(Date createDate) {
    this.createDate = createDate;
    return this;
  }

  public TenancyDto setLastUpdate(Date lastUpdate) {
    this.lastUpdate = lastUpdate;
    return this;
  }
}
