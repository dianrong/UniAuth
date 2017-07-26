package com.dianrong.common.uniauth.common.jwt.exp;

/**
 *登陆的JWT生成失败的异常.
 *
 * @author wanglin
 */
public class InvalidJWTExpiredException extends Exception {

  private static final long serialVersionUID = 8747369421016269942L;

  public InvalidJWTExpiredException() {
    super();
  }

  public InvalidJWTExpiredException(String msg) {
    super(msg);
  }

  public InvalidJWTExpiredException(String msg, Throwable t) {
    super(msg, t);
  }
}
