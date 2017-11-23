package com.dianrong.common.techops.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 没有找到对应的Api.
 */
public class InvalidParameterException extends UniauthCommonException {

  private static final long serialVersionUID = 725349568285975577L;

  public InvalidParameterException() {
    super();
  }

  public InvalidParameterException(String msg) {
    super(msg);
  }

  public InvalidParameterException(String msg, Throwable t) {
    super(msg, t);
  }
}
