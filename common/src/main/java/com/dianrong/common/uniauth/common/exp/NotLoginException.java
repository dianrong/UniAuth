package com.dianrong.common.uniauth.common.exp;

public class NotLoginException extends RuntimeException {

  private static final long serialVersionUID = -3937666101048156666L;

  public NotLoginException(String message) {
    super(message);
  }
}
