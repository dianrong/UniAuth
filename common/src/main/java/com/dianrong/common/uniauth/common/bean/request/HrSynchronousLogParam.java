package com.dianrong.common.uniauth.common.bean.request;

import lombok.ToString;

import java.util.Date;

/**
 * 同步操作的日志查询参数.
 */
@ToString
public class HrSynchronousLogParam extends PageParam {
  private static final long serialVersionUID = -7961564170283106319L;

  private Long id;
  private Date startTime;
  private Date endTime;
  private String type;
  private String computerIp;
  private String result;
  /**
   * 是否异步执行.
   */
  private Boolean asynchronous;

  public Long getId() {
    return id;
  }

  public HrSynchronousLogParam setId(Long id) {
    this.id = id;
    return this;
  }

  public Date getStartTime() {
    return startTime;
  }

  public HrSynchronousLogParam setStartTime(Date startTime) {
    this.startTime = startTime;
    return this;
  }

  public Date getEndTime() {
    return endTime;
  }

  public HrSynchronousLogParam setEndTime(Date endTime) {
    this.endTime = endTime;
    return this;
  }

  public String getType() {
    return type;
  }

  public HrSynchronousLogParam setType(String type) {
    this.type = type;
    return this;
  }

  public String getComputerIp() {
    return computerIp;
  }

  public HrSynchronousLogParam setComputerIp(String computerIp) {
    this.computerIp = computerIp;
    return this;
  }

  public String getResult() {
    return result;
  }

  public HrSynchronousLogParam setResult(String result) {
    this.result = result;
    return this;
  }

  public Boolean getAsynchronous() {
    return asynchronous;
  }

  public HrSynchronousLogParam setAsynchronous(Boolean asynchronous) {
    this.asynchronous = asynchronous;
    return this;
  }
}
