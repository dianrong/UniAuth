package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * 权限不足.
 *
 * @author wanglin
 */
public class InsufficientPrivilegesException extends AccessDeniedException {

  private static final long serialVersionUID = 6270991049405635507L;

  public InsufficientPrivilegesException() {
    super();
  }

  public InsufficientPrivilegesException(String msg) {
    super(msg);
  }

  public InsufficientPrivilegesException(String msg, Throwable t) {
    super(msg, t);
  }
}
