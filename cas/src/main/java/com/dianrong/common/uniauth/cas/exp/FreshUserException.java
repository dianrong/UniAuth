package com.dianrong.common.uniauth.cas.exp;

import javax.security.auth.login.AccountException;

public class FreshUserException extends AccountException {

  private static final long serialVersionUID = 1665206621245374336L;

  public FreshUserException() {
    super();
  }

  public FreshUserException(String msg) {
    super(msg);
  }
}
