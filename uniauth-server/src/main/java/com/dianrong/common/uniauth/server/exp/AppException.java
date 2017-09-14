package com.dianrong.common.uniauth.server.exp;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.exp.UniauthException;

public class AppException extends UniauthException {

  private static final long serialVersionUID = -8540649906647227589L;

  private InfoName infoName;
  private String msg;

  public AppException(InfoName infoName) {
    this(infoName, null);
  }

  public AppException(InfoName infoName, String msg) {
    this(infoName, msg, null);
  }

  public AppException(InfoName infoName, String msg, Throwable t) {
    super(msg, t);
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
