package com.dianrong.common.uniauth.common.exp;

/**
 * Uniauth common exception.
 *
 * @author wanglin
 */
public class UniauthCommonException extends RuntimeException {

  private static final long serialVersionUID = -6995860431603669132L;

  /**
   * Define a empty method.
   */
  public UniauthCommonException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthCommonException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthCommonException(String msg, Throwable t) {
    super(msg, t);
  }
}
