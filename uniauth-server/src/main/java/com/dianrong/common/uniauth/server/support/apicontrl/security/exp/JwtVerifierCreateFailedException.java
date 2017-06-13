package com.dianrong.common.uniauth.server.support.apicontrl.security.exp;

/**
 * Failed to create JWTVerifier exception.
 *
 * @author wanglin
 */
public class JwtVerifierCreateFailedException extends Exception {

  private static final long serialVersionUID = 8747369421016269942L;

  public JwtVerifierCreateFailedException() {
    super();
  }

  public JwtVerifierCreateFailedException(String msg) {
    super(msg);
  }

  public JwtVerifierCreateFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
