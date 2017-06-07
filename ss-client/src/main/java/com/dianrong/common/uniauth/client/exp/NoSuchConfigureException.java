package com.dianrong.common.uniauth.client.exp;

/**
 * there is no configure for creating bean
 *
 * @author wanglin
 */
public class NoSuchConfigureException extends RuntimeException {

  private static final long serialVersionUID = 8383591759173836181L;

  public NoSuchConfigureException() {
  }

  public NoSuchConfigureException(String msg) {
    super(msg);
  }
}
