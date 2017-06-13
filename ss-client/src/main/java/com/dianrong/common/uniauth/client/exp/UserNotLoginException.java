package com.dianrong.common.uniauth.client.exp;

public class UserNotLoginException extends RuntimeException {

  private static final long serialVersionUID = -850695620176807010L;

  public UserNotLoginException(String message) {
    super(message);
  }
}
