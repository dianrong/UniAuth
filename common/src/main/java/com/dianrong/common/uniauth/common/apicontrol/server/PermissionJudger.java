package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

/**
 * Judge credential's permission is passed.
 *
 * @author wanglin
 */
public interface PermissionJudger<T extends Serializable, E> {

  /**
   * Decide whether the credential has permission to access the resource.
   *
   * @param credential Credential
   * @param resource the resource
   * @return true or false
   */
  boolean judge(CallerCredential<T> credential, E resource);
}
