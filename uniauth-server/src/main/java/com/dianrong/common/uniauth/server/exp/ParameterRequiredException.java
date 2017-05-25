package com.dianrong.common.uniauth.server.exp;

/**
 * 参数缺失的异常类型定义.
 * 
 * @author wanglin
 */
public class ParameterRequiredException extends RuntimeException {
  private static final long serialVersionUID = -8540649906647227589L;

  public ParameterRequiredException(String msg) {
    super(msg);
  }
}
