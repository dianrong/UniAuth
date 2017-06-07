package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

import com.dianrong.common.uniauth.common.apicontrol.exp.LoadCredentialFailedException;

/**
 * Load WillExpiredCallerCredential
 *
 * @author wanglin
 */
public interface LoadCredential<T extends Serializable> {

  /**
   * Load Credential by account and password
   *
   * @param account account
   * @param password password
   * @return CallerCredential
   * @throws LoadCredentialFailedException load credential failed
   */
  CallerCredential<T> loadCredential(String account, String password)
      throws LoadCredentialFailedException;
}
