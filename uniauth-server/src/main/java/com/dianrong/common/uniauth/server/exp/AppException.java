package com.dianrong.common.uniauth.server.exp;

import com.dianrong.common.uniauth.common.bean.InfoName;

public class AppException extends RuntimeException {

  private static final long serialVersionUID = -8540649906647227589L;

  private InfoName infoName;
  private String msg;

  /**
   * 构造一个AppException.
   */
  public AppException(InfoName infoName, String msg) {
    super(msg);
    this.infoName = infoName;
    this.msg = msg;
  }

  public InfoName getInfoName() {
    return infoName;
  }

  public void setInfoName(InfoName infoName) {
    this.infoName = infoName;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

}
