package com.dianrong.common.uniauth.cas.exp;

public class ValidateFailException extends Exception {

  private static final long serialVersionUID = 1665206621245374336L;

  public ValidateFailException() {
    super();
  }

  public ValidateFailException(String msg) {
    super(msg);
  }
}
