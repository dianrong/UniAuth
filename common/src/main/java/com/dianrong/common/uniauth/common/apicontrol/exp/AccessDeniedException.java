package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * 访问拒绝异常.
 *
 * @author wanglin
 */
public class AccessDeniedException extends RuntimeException {

  private static final long serialVersionUID = 5839189788879696684L;

  public AccessDeniedException() {
    super();
  }

  public AccessDeniedException(String msg) {
    super(msg);
  }

  public AccessDeniedException(String msg, Throwable t) {
    super(msg, t);
  }
}
