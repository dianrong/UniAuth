package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

/**
 * 调用者的信息
 *
 * @author wanglin
 */
public interface CallerCredential<T extends Serializable> extends Serializable, WillExpired {

  /**
   * return caller name
   */
  String getCallerName();

  /**
   * get account
   */
  String getAccount();

  /**
   * get permission information
   */
  T getPermissionInfo();
}
