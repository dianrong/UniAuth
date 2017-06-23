package com.dianrong.common.uniauth.common.exp;

public class LoginFailedException extends RuntimeException {

  private static final long serialVersionUID = 874400685387636837L;

  public LoginFailedException(String message) {
    super(message);
  }

}
