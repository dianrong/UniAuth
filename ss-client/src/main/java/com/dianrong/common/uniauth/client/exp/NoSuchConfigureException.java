package com.dianrong.common.uniauth.client.exp;

import com.dianrong.common.uniauth.common.exp.UniauthException;

public class NoSuchConfigureException extends UniauthException {

  private static final long serialVersionUID = 8383591759173836181L;

  public NoSuchConfigureException() {
  }

  public NoSuchConfigureException(String msg) {
    super(msg);
  }
}
