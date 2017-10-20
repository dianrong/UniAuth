package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * UCM idc client创建失败.
 */
public class IdcClientCreateFailureException extends UniauthCommonException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * Define a empty method.
   */
  public IdcClientCreateFailureException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public IdcClientCreateFailureException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public IdcClientCreateFailureException(String msg, Throwable t) {
    super(msg, t);
  }
}
