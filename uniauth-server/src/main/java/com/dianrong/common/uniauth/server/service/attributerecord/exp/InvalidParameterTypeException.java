package com.dianrong.common.uniauth.server.service.attributerecord.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 在Handler处理过程中Identity或ExtendId类型错误抛出的异常.
 */
public class InvalidParameterTypeException extends UniauthCommonException {

  private static final long serialVersionUID = 6742145350967709995L;

  public InvalidParameterTypeException() {
    super();
  }

  public InvalidParameterTypeException(String msg) {
    super(msg);
  }

  public InvalidParameterTypeException(String msg, Throwable t) {
    super(msg, t);
  }
}
