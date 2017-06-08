package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * Token is expired.
 *
 * @author wanglin
 */
public class TokenExpiredException extends Exception {

  private static final long serialVersionUID = -4557600302758367094L;

  public TokenExpiredException() {
    super();
  }

  public TokenExpiredException(String msg) {
    super(msg);
  }

  public TokenExpiredException(String msg, Throwable t) {
    super(msg, t);
  }
}
