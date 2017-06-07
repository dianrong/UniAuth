package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

import com.dianrong.common.uniauth.common.apicontrol.exp.InvalidTokenException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenCreateFailedException;
import com.dianrong.common.uniauth.common.apicontrol.exp.TokenExpiredException;

/**
 * token process interface
 *
 * @param <T> WillExpiredCallerCredential type
 * @author wanglin
 */
public interface TokenProcessor<T extends Serializable> {

  /**
   * generate token from WillExpiredCallerCredential
   *
   * @param credential credential
   * @return token
   * @throws TokenCreateFailedException if create token failed
   */
  String sign(CallerCredential<T> credential) throws TokenCreateFailedException;

  /**
   * verify token
   *
   * @param token can not be null
   * @return WillExpiredCallerCredential
   * @throws InvalidTokenException if token is invalid
   * @throws TokenExpiredException if token is expired
   */
  CallerCredential<T> verify(String token) throws InvalidTokenException, TokenExpiredException;
}
