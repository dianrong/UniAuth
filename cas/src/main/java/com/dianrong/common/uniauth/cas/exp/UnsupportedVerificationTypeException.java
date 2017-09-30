package com.dianrong.common.uniauth.cas.exp;

import com.dianrong.common.uniauth.common.exp.UniauthException;

public class UnsupportedVerificationTypeException extends UniauthException {

  private static final long serialVersionUID = 1665206621245374336L;

  public UnsupportedVerificationTypeException() {
    super();
  }

  public UnsupportedVerificationTypeException(String msg) {
    super(msg);
  }
}
