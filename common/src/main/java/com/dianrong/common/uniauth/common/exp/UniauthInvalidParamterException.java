package com.dianrong.common.uniauth.common.exp;

/**
 * 入参类型不对的异常.
 *
 * @author wanglin
 */
public class UniauthInvalidParamterException extends RuntimeException {

  private static final long serialVersionUID = -6995860431603669132L;

  /**
   * Define a empty method.
   */
  public UniauthInvalidParamterException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthInvalidParamterException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public UniauthInvalidParamterException(String msg, Throwable t) {
    super(msg, t);
  }
}
