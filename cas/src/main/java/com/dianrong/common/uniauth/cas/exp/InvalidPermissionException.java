package com.dianrong.common.uniauth.cas.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 权限不对.
 *
 * @author wanglin
 */
public class InvalidPermissionException extends UniauthCommonException {

  private static final long serialVersionUID = 1665206621245374336L;

  public InvalidPermissionException() {
    super();
  }

  public InvalidPermissionException(String msg) {
    super(msg);
  }

  public InvalidPermissionException(String msg, Throwable t) {
    super(msg, t);
  }
}
