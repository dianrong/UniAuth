package com.dianrong.common.uniauth.common.client.cxf;

import com.dianrong.common.uniauth.common.util.Assert;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 用于处理访问的控制处理逻辑.
 *
 * @author wanglin
 */
public final class ApiControlInvocationHandler implements InvocationHandler {

  /**
   * 目标对象.
   */
  private final Object target;

  public ApiControlInvocationHandler(Object target) {
    Assert.notNull(target);
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    return ApiCallCtlManager.getInstance().getInvoker().invoke(target, proxy, method, args);
  }
}
