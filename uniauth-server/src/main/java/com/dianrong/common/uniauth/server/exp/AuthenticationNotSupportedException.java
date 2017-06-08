package com.dianrong.common.uniauth.server.exp;

import com.dianrong.common.uniauth.common.bean.InfoName;

/**
 * 不支持的数据验证异常.
 * 
 * @author wanglin
 */
public class AuthenticationNotSupportedException extends AppException {
  private static final long serialVersionUID = -8540649906647227589L;

  public AuthenticationNotSupportedException(String msg) {
    super(InfoName.BAD_REQUEST, msg);
  }
}
