package com.dianrong.common.techops.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 没有找到对应的Api.
 */
public class NotFoundApiException extends UniauthCommonException {

  private static final long serialVersionUID = 725349568285975577L;

  public NotFoundApiException() {
    super();
  }

  public NotFoundApiException(String msg) {
    super(msg);
  }

  public NotFoundApiException(String msg, Throwable t) {
    super(msg, t);
  }
}
