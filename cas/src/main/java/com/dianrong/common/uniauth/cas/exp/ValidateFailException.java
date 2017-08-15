package com.dianrong.common.uniauth.cas.exp;

import com.dianrong.common.uniauth.common.exp.UniauthException;

public class ValidateFailException extends UniauthException {

  private static final long serialVersionUID = 1665206621245374336L;

  public ValidateFailException() {
    super();
  }

  public ValidateFailException(String msg) {
    super(msg);
  }
}
