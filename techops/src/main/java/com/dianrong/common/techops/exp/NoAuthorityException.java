package com.dianrong.common.techops.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 没有权限访问.
 */
public class NoAuthorityException extends UniauthCommonException {

  private static final long serialVersionUID = 725349568285975577L;

  public NoAuthorityException() {
    super();
  }

  public NoAuthorityException(String msg) {
    super(msg);
  }

  public NoAuthorityException(String msg, Throwable t) {
    super(msg, t);
  }
}
