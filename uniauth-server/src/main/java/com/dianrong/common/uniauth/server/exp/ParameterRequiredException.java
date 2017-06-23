package com.dianrong.common.uniauth.server.exp;

import com.dianrong.common.uniauth.common.bean.InfoName;

/**
 * 参数缺失的异常类型定义.
 *
 * @author wanglin
 */
public class ParameterRequiredException extends AppException {
  private static final long serialVersionUID = -8540649906647227589L;

  public ParameterRequiredException(String msg) {
    super(InfoName.BAD_REQUEST, msg);
  }
}
