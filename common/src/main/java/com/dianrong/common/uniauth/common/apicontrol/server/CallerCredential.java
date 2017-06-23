package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

/**
 * 调用者的信息.
 *
 * @author wanglin
 */
public interface CallerCredential<T extends Serializable> extends Serializable, WillExpired {

  /**
   * Return caller name.
   */
  String getCallerName();

  /**
   * Get account.
   */
  String getAccount();

  /**
   * Get permission information.
   */
  T getPermissionInfo();
}
