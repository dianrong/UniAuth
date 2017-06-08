package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * Token is invalid.
 *
 * @author wanglin
 */
public class InvalidTokenException extends Exception {

  private static final long serialVersionUID = -4557600302758367094L;

  private String token;

  public String getToken() {
    return token;
  }

  public InvalidTokenException() {
    super();
  }

  public InvalidTokenException(String msg) {
    super(msg);
  }

  public InvalidTokenException(String msg, String token) {
    super(msg);
    this.token = token;
  }

  public InvalidTokenException(String msg, Throwable t) {
    super(msg, t);
  }

  public InvalidTokenException(String msg, Throwable t, String token) {
    super(msg, t);
    this.token = token;
  }
}
