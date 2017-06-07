package com.dianrong.common.uniauth.common.exp;

/**
 * uniauth common exception
 *
 * @author wanglin
 */
public class UniauthCommonException extends RuntimeException {

  private static final long serialVersionUID = -6995860431603669132L;

  /**
   * define a empty method
   */
  public UniauthCommonException() {
    super();
  }

  /**
   * define method for parameter msg
   *
   * @param msg msg
   */
  public UniauthCommonException(String msg) {
    super(msg);
  }

  /**
   * define method for parameter msg
   *
   * @param msg msg
   */
  public UniauthCommonException(String msg, Throwable t) {
    super(msg, t);
  }
}
