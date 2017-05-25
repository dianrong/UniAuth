package com.dianrong.common.uniauth.server.track;

import java.util.Date;

public class GlobalVar implements Cloneable {

  // operator id
  private Long userId;
  private String ip;
  private String uuid;
  private String reqUrl;
  private String method;
  private String exception;
  private Byte success;
  private Long elapse;
  private Date reqDate;
  private String reqParam;
  private String mapper;
  private Long invokeSeq;
  private Integer domainId;
  private Long tenancyId;
  private String requestDomainCode;

  public Integer getDomainId() {
    return domainId;
  }

  public void setDomainId(Integer domainId) {
    this.domainId = domainId;
  }

  public Long getInvokeSeq() {
    return invokeSeq;
  }

  public void setInvokeSeq(Long invokeSeq) {
    this.invokeSeq = invokeSeq;
  }

  public String getMapper() {
    return mapper;
  }

  public void setMapper(String mapper) {
    this.mapper = mapper;
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String getReqParam() {
    return reqParam;
  }

  public void setReqParam(String reqParam) {
    this.reqParam = reqParam;
  }

  public Long getElapse() {
    return elapse;
  }

  public void setElapse(Long elapse) {
    this.elapse = elapse;
  }

  public GlobalVar() {

  }

  public Byte getSuccess() {
    return success;
  }

  public void setSuccess(Byte success) {
    this.success = success;
  }

  public String getException() {
    return exception;
  }


  public void setException(String exception) {
    this.exception = exception;
  }


  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }


  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getReqUrl() {
    return reqUrl;
  }

  public void setReqUrl(String reqUrl) {
    this.reqUrl = reqUrl;
  }

  public Date getReqDate() {
    return reqDate;
  }

  public void setReqDate(Date reqDate) {
    this.reqDate = reqDate;
  }

  public Long getTenancyId() {
    return tenancyId;
  }

  public void setTenancyId(Long tenancyId) {
    this.tenancyId = tenancyId;
  }

  public String getRequestDomainCode() {
    return requestDomainCode;
  }

  public void setRequestDomainCode(String requestDomainCode) {
    this.requestDomainCode = requestDomainCode;
  }
}
