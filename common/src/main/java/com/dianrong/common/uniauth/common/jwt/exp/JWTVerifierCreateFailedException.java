package com.dianrong.common.uniauth.common.jwt.exp;

/**
 *创建JWTVerifierCreateFailedException失败.
 *
 * @author wanglin
 */
public class JWTVerifierCreateFailedException extends Exception {

  private static final long serialVersionUID = 8747369421016269942L;

  public JWTVerifierCreateFailedException() {
    super();
  }

  public JWTVerifierCreateFailedException(String msg) {
    super(msg);
  }

  public JWTVerifierCreateFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
