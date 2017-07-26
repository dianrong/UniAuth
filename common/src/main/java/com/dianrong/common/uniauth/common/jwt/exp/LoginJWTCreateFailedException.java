package com.dianrong.common.uniauth.common.jwt.exp;

/**
 *登陆的JWT生成失败的异常.
 *
 * @author wanglin
 */
public class LoginJWTCreateFailedException extends Exception {

  private static final long serialVersionUID = 8747369421016269942L;

  public LoginJWTCreateFailedException() {
    super();
  }

  public LoginJWTCreateFailedException(String msg) {
    super(msg);
  }

  public LoginJWTCreateFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
