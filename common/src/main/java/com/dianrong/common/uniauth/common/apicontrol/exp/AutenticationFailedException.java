package com.dianrong.common.uniauth.common.apicontrol.exp;

/**
 * 身份认证失败异常.
 *
 * @author wanglin
 */
public class AutenticationFailedException extends AccessDeniedException {

  private static final long serialVersionUID = 6270991049405635507L;

  public AutenticationFailedException() {
    super();
  }

  public AutenticationFailedException(String msg) {
    super(msg);
  }

  public AutenticationFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
