package com.dianrong.common.uniauth.common.exp;

/**
 * Uniauth里面的异常类型.
 * 
 * @author wanglin
 *
 */
public class UniauthException extends RuntimeException {

  private static final long serialVersionUID = -6995860431603669132L;

  /**
   * Define a empty method.
   */
  public UniauthException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthException(String msg, Throwable t) {
    super(msg, t);
  }
}
