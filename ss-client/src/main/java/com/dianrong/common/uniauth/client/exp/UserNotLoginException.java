package com.dianrong.common.uniauth.client.exp;

import com.dianrong.common.uniauth.common.exp.UniauthException;

public class UserNotLoginException extends UniauthException {

  private static final long serialVersionUID = -850695620176807010L;

  public UserNotLoginException(String message) {
    super(message);
  }
}
