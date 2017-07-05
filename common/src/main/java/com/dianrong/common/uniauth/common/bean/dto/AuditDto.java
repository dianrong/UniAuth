package com.dianrong.common.uniauth.common.bean.dto;

import java.util.Date;

import lombok.ToString;

/**
 * Created by Arc on 24/3/2016.
 */
@ToString
public class AuditDto extends TenancyBaseDto {

  private static final long serialVersionUID = -1081049216491108772L;
  private Integer id;
  private Long userId;
  private Date requestDate;
  private Integer domainId;
  private String reqIp;
  private String reqUuid;
  private String reqUrl;
  private Long reqSequence;
  private String reqClass;
  private String reqMethod;
  private Byte reqSuccess;
  private String reqException;
  private Long reqElapse;
  private String reqParam;
  private String reqResult;

  public Integer getId() {
    return id;
  }

  public AuditDto setId(Integer id) {
    this.id = id;
    return this;
  }

  public Long getUserId() {
    return userId;
  }

  public AuditDto setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public AuditDto setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public AuditDto setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public String getReqIp() {
    return reqIp;
  }

  public AuditDto setReqIp(String reqIp) {
    this.reqIp = reqIp;
    return this;
  }

  public String getReqUuid() {
    return reqUuid;
  }

  public AuditDto setReqUuid(String reqUuid) {
    this.reqUuid = reqUuid;
    return this;
  }

  public String getReqUrl() {
    return reqUrl;
  }

  public AuditDto setReqUrl(String reqUrl) {
    this.reqUrl = reqUrl;
    return this;
  }

  public Long getReqSequence() {
    return reqSequence;
  }

  public AuditDto setReqSequence(Long reqSequence) {
    this.reqSequence = reqSequence;
    return this;
  }

  public String getReqClass() {
    return reqClass;
  }

  public AuditDto setReqClass(String reqClass) {
    this.reqClass = reqClass;
    return this;
  }

  public String getReqMethod() {
    return reqMethod;
  }

  public AuditDto setReqMethod(String reqMethod) {
    this.reqMethod = reqMethod;
    return this;
  }

  public Byte getReqSuccess() {
    return reqSuccess;
  }

  public AuditDto setReqSuccess(Byte reqSuccess) {
    this.reqSuccess = reqSuccess;
    return this;
  }

  public String getReqException() {
    return reqException;
  }

  public AuditDto setReqException(String reqException) {
    this.reqException = reqException;
    return this;
  }

  public Long getReqElapse() {
    return reqElapse;
  }

  public AuditDto setReqElapse(Long reqElapse) {
    this.reqElapse = reqElapse;
    return this;
  }

  public String getReqParam() {
    return reqParam;
  }

  public AuditDto setReqParam(String reqParam) {
    this.reqParam = reqParam;
    return this;
  }

  public String getReqResult() {
    return reqResult;
  }

  public AuditDto setReqResult(String reqResult) {
    this.reqResult = reqResult;
    return this;
  }
}
