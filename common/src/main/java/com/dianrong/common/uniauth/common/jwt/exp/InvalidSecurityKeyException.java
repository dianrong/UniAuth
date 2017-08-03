package com.dianrong.common.uniauth.common.jwt.exp;

/**
 *传入的RSA的秘钥字符串不规范.
 *
 * @author wanglin
 */
public class InvalidSecurityKeyException extends Exception {

  private static final long serialVersionUID = 8747369421016269942L;

  public InvalidSecurityKeyException() {
    super();
  }

  public InvalidSecurityKeyException(String msg) {
    super(msg);
  }

  public InvalidSecurityKeyException(String msg, Throwable t) {
    super(msg, t);
  }
}
