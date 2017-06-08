package com.dianrong.common.uniauth.common.apicontrol.client;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import java.lang.reflect.Method;
import org.slf4j.Logger;

/**
 * 模板方法，处理调用handler逻辑.
 *
 * @author wanglin
 */
public abstract class AbstractInvokeHandlerDelegate implements InvokeHandlerDelegate {

  private static final Logger LOGGER = org.slf4j.LoggerFactory
      .getLogger(AppConstants.UNIAUTH_API_CALL_TIME_OUT_LOGGER);

  @Override
  public Object invoke(Object target, Object proxy, Method method, Object[] args) throws Throwable {
    Object result = null;
    Throwable cause = null;
    long beforeInvoke = System.currentTimeMillis();
    try {
      beforeInvoke(target, proxy, method, args);
      try {
        result = doInvoke(target, proxy, method, args);
      } catch (Throwable t) {
        cause = t;
      }
      return afterInvoke(target, proxy, method, args, result, cause);
    } finally {
      if (cause != null) {
        LOGGER
            .warn("{} invoke consume {} milles", method, System.currentTimeMillis() - beforeInvoke);
      }
    }
  }

  /**
   * Real method invoker.
   *
   * @param target target
   * @param proxy proxy
   * @param method method
   * @param args args
   * @return invoke result
   * @throws Throwable throwable
   */
  protected Object doInvoke(Object target, Object proxy, Method method, Object[] args)
      throws Throwable {
    return method.invoke(target, args);
  }

  public abstract void beforeInvoke(Object target, Object proxy, Method method, Object[] args);


  public abstract Object afterInvoke(Object target, Object proxy, Method method, Object[] args,
      Object result, Throwable cause) throws Throwable;
}
