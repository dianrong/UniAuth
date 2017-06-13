package com.dianrong.common.uniauth.common.client.cxf.exp;

/**
 * 非法的header key使用异常.
 *
 * @author wanglin
 */
public class InvalidHeaderKeyException extends RuntimeException {

  private static final long serialVersionUID = 5839189788879696684L;

  public InvalidHeaderKeyException() {
    super();
  }

  public InvalidHeaderKeyException(String msg) {
    super(msg);
  }

  public InvalidHeaderKeyException(String msg, Throwable t) {
    super(msg, t);
  }
}
