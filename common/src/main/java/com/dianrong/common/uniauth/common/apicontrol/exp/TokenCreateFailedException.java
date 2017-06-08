package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * Create token failed exception.
 *
 * @author wanglin
 */
public class TokenCreateFailedException extends Exception {

  private static final long serialVersionUID = -4557600302758367094L;

  public TokenCreateFailedException() {
    super();
  }

  public TokenCreateFailedException(String msg) {
    super(msg);
  }

  public TokenCreateFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
