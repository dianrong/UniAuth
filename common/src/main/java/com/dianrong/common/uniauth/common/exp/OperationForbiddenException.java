package com.dianrong.common.uniauth.common.exp;

public class OperationForbiddenException extends RuntimeException {

  private static final long serialVersionUID = 4337200288278667300L;

  public OperationForbiddenException(String message) {
    super(message);
  }

}

