package com.dianrong.common.uniauth.common.exp;

public class NotReuseSessionIdException extends RuntimeException {

  private static final long serialVersionUID = -8156444442353432842L;

  public NotReuseSessionIdException(String message) {
    super(message);
  }

}
