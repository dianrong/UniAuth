package com.dianrong.common.uniauth.common.bean;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by Arc on 14/1/16.
 */
@ApiModel("请求失败异常信息")
public final class Info implements Serializable {

  private static final long serialVersionUID = -1527808041715597462L;

  @ApiModelProperty(value = "失败的异常类型", required = true)
  private InfoName name;
  @ApiModelProperty("异常详细信息")
  private String msg;

  // Note: default constructor is mandatory!!!
  public Info() {

  }

  public Info(InfoName name) {
    this.name = name;
  }

  public Info(InfoName name, String msg) {
    this.name = name;
    this.msg = msg;
  }

  public InfoName getName() {
    return name;
  }

  public String getMsg() {
    return msg;
  }

  public static Info build(InfoName name, String msg) {
    return new Info(name, msg);
  }
}
