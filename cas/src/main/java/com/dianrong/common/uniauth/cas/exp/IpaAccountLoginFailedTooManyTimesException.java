package com.dianrong.common.uniauth.cas.exp;

import javax.security.auth.login.AccountException;

public class IpaAccountLoginFailedTooManyTimesException extends AccountException {

  private static final long serialVersionUID = 9200032715119269329L;

  public IpaAccountLoginFailedTooManyTimesException() {
    super();
  }

  public IpaAccountLoginFailedTooManyTimesException(String msg) {
    super(msg);
  }
}
