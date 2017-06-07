package com.dianrong.common.uniauth.cas.exp;

import javax.security.auth.login.AccountException;

public class MultiUsersFoundException extends AccountException {

  private static final long serialVersionUID = -1716258367838958640L;

  public MultiUsersFoundException() {
    super();
  }

  public MultiUsersFoundException(String msg) {
    super(msg);
  }
}
