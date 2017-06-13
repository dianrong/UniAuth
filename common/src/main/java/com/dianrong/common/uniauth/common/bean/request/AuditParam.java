package com.dianrong.common.uniauth.common.bean.request;

import java.util.Date;

/**
 * Created by Arc on 24/3/2016.
 */
public class AuditParam extends PageParam {

  private static final long serialVersionUID = 8772883664818679757L;
  private Long userId;
  private Date minRequestDate;
  private Date maxRequestDate;
  private Integer domainId;
  private String reqIp;
  private String reqUuid;
  private String reqUrl;
  private Long reqSequence;
  private String reqClass;
  private String reqMethod;
  private Byte reqSuccess;
  private String reqException;
  private Long minReqElapse;
  private Long maxReqElapse;
  private String reqParam;
  private String reqResult;
  private String orderBy;
  private Boolean ascOrDesc;
  private String requestDomainCode;

  public Long getUserId() {
    return userId;
  }

  public AuditParam setUserId(Long userId) {
    this.userId = userId;
    return this;
  }

  public Date getMinRequestDate() {
    return minRequestDate;
  }

  public AuditParam setMinRequestDate(Date minRequestDate) {
    this.minRequestDate = minRequestDate;
    return this;
  }

  public Date getMaxRequestDate() {
    return maxRequestDate;
  }

  public AuditParam setMaxRequestDate(Date maxRequestDate) {
    this.maxRequestDate = maxRequestDate;
    return this;
  }

  public Integer getDomainId() {
    return domainId;
  }

  public AuditParam setDomainId(Integer domainId) {
    this.domainId = domainId;
    return this;
  }

  public String getReqIp() {
    return reqIp;
  }

  public AuditParam setReqIp(String reqIp) {
    this.reqIp = reqIp;
    return this;
  }

  public String getReqUuid() {
    return reqUuid;
  }

  public AuditParam setReqUuid(String reqUuid) {
    this.reqUuid = reqUuid;
    return this;
  }

  public String getReqUrl() {
    return reqUrl;
  }

  public AuditParam setReqUrl(String reqUrl) {
    this.reqUrl = reqUrl;
    return this;
  }

  public Long getReqSequence() {
    return reqSequence;
  }

  public AuditParam setReqSequence(Long reqSequence) {
    this.reqSequence = reqSequence;
    return this;
  }

  public String getReqClass() {
    return reqClass;
  }

  public AuditParam setReqClass(String reqClass) {
    this.reqClass = reqClass;
    return this;
  }

  public String getReqMethod() {
    return reqMethod;
  }

  public AuditParam setReqMethod(String reqMethod) {
    this.reqMethod = reqMethod;
    return this;
  }

  public Byte getReqSuccess() {
    return reqSuccess;
  }

  public AuditParam setReqSuccess(Byte reqSuccess) {
    this.reqSuccess = reqSuccess;
    return this;
  }

  public String getReqException() {
    return reqException;
  }

  public AuditParam setReqException(String reqException) {
    this.reqException = reqException;
    return this;
  }

  public Long getMinReqElapse() {
    return minReqElapse;
  }

  public AuditParam setMinReqElapse(Long minReqElapse) {
    this.minReqElapse = minReqElapse;
    return this;
  }

  public Long getMaxReqElapse() {
    return maxReqElapse;
  }

  public AuditParam setMaxReqElapse(Long maxReqElapse) {
    this.maxReqElapse = maxReqElapse;
    return this;
  }

  public String getReqParam() {
    return reqParam;
  }

  public AuditParam setReqParam(String reqParam) {
    this.reqParam = reqParam;
    return this;
  }

  public String getReqResult() {
    return reqResult;
  }

  public AuditParam setReqResult(String reqResult) {
    this.reqResult = reqResult;
    return this;
  }

  public String getOrderBy() {
    return orderBy;
  }

  public AuditParam setOrderBy(String orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public Boolean getAscOrDesc() {
    return ascOrDesc;
  }

  public AuditParam setAscOrDesc(Boolean ascOrDesc) {
    this.ascOrDesc = ascOrDesc;
    return this;
  }

  public String getRequestDomainCode() {
    return requestDomainCode;
  }

  public AuditParam setRequestDomainCode(String requestDomainCode) {
    this.requestDomainCode = requestDomainCode;
    return this;
  }

  @Override
  public String toString() {
    return "AuditParam [userId=" + userId + ", minRequestDate=" + minRequestDate
        + ", maxRequestDate=" + maxRequestDate + ", domainId=" + domainId + ", reqIp=" + reqIp
        + ", reqUuid=" + reqUuid + ", reqUrl=" + reqUrl + ", reqSequence=" + reqSequence
        + ", reqClass=" + reqClass + ", reqMethod=" + reqMethod + ", reqSuccess="
        + reqSuccess + ", reqException=" + reqException + ", minReqElapse=" + minReqElapse
        + ", maxReqElapse=" + maxReqElapse + ", reqParam=" + reqParam + ", reqResult="
        + reqResult + ", orderBy=" + orderBy + ", ascOrDesc=" + ascOrDesc + ", requestDomainCode="
        + requestDomainCode + "]";
  }
}
