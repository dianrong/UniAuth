package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

public class InvalidContentException extends UniauthCommonException{

  private static final long serialVersionUID = -2876152277599263785L;

  /**
   * Define a empty method.
   */
  public InvalidContentException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public InvalidContentException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public InvalidContentException(String msg, Throwable t) {
    super(msg, t);
  }
}
