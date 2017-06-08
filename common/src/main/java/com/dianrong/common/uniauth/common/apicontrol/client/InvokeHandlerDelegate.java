package com.dianrong.common.uniauth.common.apicontrol.client;

import java.lang.reflect.Method;

/**
 * Delegate for invokeHandler.
 *
 * @author wanglin
 */
public interface InvokeHandlerDelegate {

  /**
   * Delegate for invoke(Object proxy, Method method, Object[] args).
   */
  Object invoke(Object target, Object proxy, Method method, Object[] args) throws Throwable;
}
