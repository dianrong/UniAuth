package com.dianrong.common.uniauth.client.custom.jwt.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 认证身份的时候, JWT不规范.
 * 
 * @author wanglin
 *
 */
public class JWTInvalidAuthenticationException extends UniauthCommonException {

  private static final long serialVersionUID = -6911304773809412849L;

  /**
   * Define a empty method.
   */
  public JWTInvalidAuthenticationException() {
    super();
  }

  public JWTInvalidAuthenticationException(String msg) {
    super(msg);
  }

  public JWTInvalidAuthenticationException(String msg, Throwable t) {
    super(msg, t);
  }
}
