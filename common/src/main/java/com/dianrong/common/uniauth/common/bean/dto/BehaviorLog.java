package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;

import lombok.ToString;

@ToString
public class BehaviorLog extends TenancyBaseDto {

  private Long id;

  private Long aid;

  private Short type;

  private String target;

  private String ipAddr;

  private Date createD;

  private Short result;

  private static final long serialVersionUID = 1L;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAid() {
    return aid;
  }

  public void setAid(Long aid) {
    this.aid = aid;
  }

  public Short getType() {
    return type;
  }

  public void setType(Short type) {
    this.type = type;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target == null ? null : target.trim();
  }

  public String getIpAddr() {
    return ipAddr;
  }

  public void setIpAddr(String ipAddr) {
    this.ipAddr = ipAddr == null ? null : ipAddr.trim();
  }

  public Date getCreateD() {
    return createD;
  }

  public void setCreateD(Date createD) {
    this.createD = createD;
  }

  public Short getResult() {
    return result;
  }

  public void setResult(Short result) {
    this.result = result;
  }
}
