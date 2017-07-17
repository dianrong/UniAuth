package com.dianrong.common.uniauth.server.service.attributerecord.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 传入的TypeOperate类型不支持抛出的异常.
 */
public class NotSupportedTypeOperateException extends UniauthCommonException {

  private static final long serialVersionUID = 6742145350967709995L;

  public NotSupportedTypeOperateException() {
    super();
  }

  public NotSupportedTypeOperateException(String msg) {
    super(msg);
  }

  public NotSupportedTypeOperateException(String msg, Throwable t) {
    super(msg, t);
  }
}
