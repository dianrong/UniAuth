package com.dianrong.common.uniauth.common.client.cxf.exp;

/**
 * 调用API的超时异常.
 *
 * @author wanglin
 */
public class ApiCallSocketTimeOutException extends RuntimeException {

  private static final long serialVersionUID = 5839189788879696684L;

  public ApiCallSocketTimeOutException() {
    super();
  }

  public ApiCallSocketTimeOutException(String msg) {
    super(msg);
  }

  public ApiCallSocketTimeOutException(String msg, Throwable t) {
    super(msg, t);
  }
}
