package com.dianrong.common.techops.service;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 处理uniauth-server的api代理处理.
 */

@Slf4j
@Service
public class ProxyInvokeService {

  @Resource
  private UARWFacade uarwFacade;

  private Map<InvokeKey, InvokedApi> readApiMap = new HashMap<>();
  private Map<InvokeKey, InvokedApi> writeApiMap = new HashMap<>();

  /**
   * 初始化方法，在使用之前需要调用.
   */
  @PostConstruct
  public void init() {
    log.info("Start collect uniauth server api list.");
    Field[] fieldArray = uarwFacade.getClass().getDeclaredFields();
    for (Field field : fieldArray) {
      Class<?> clz = field.getDeclaringClass();
      String clzName = clz.getSimpleName();
      if (clz.isInterface() && clzName.startsWith("I") && clzName.endsWith("Resource")) {
        parseApiMap(field);
      }
    }
    log.info("Finish collect uniauth server api list.");
  }

  private void parseApiMap(Field field) {
    Class<?> clz = field.getDeclaringClass();
    Method[] methodList = clz.getDeclaredMethods();
    String clzName = clz.getSimpleName();
    Class<?> readClz;
    Class<?> writeClz;
    if (clzName.contains("RW")) {
      writeClz = clz;
      Class<?> superClz = writeClz.getSuperclass();
      if (!superClz.equals(Object.class)) {
        readClz = superClz;
      } else {
        readClz = null;
      }
    } else {
      readClz = clz;
      writeClz = null;
    }
    for (Method method : methodList) {
      Class<?> returnClz = method.getReturnType();
      if (Response.class.isAssignableFrom(returnClz)) {

      }
    }
  }

  private class InvokeKey {

    private final String method;
    private final String path;

    InvokeKey(String method, String path) {
      Assert.notNull(method);
      Assert.notNull(path);
      this.method = method;
      this.path = path;
    }

    public String getMethod() {
      return method;
    }

    public String getPath() {
      return path;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      InvokeKey invokeKey = (InvokeKey) o;

      if (!method.equals(invokeKey.method)) {
        return false;
      }
      return path.equals(invokeKey.path);
    }

    @Override
    public int hashCode() {
      int result = method.hashCode();
      result = 31 * result + path.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return "InvokeKey{" +
          "method='" + method + '\'' +
          ", path='" + path + '\'' +
          '}';
    }
  }

  /**
   * 被调用的Api.
   */
  private class InvokedApi {

    private final Object target;

    private final Method invokeMethod;

    private final Class<?> paramClz;

    InvokedApi(Object target, Method invokeMethod) {
      Assert.notNull(target);
      Assert.notNull(invokeMethod);
      this.target = target;
      this.invokeMethod = invokeMethod;
      Class<?>[] parameterTypes = invokeMethod.getParameterTypes();
      if (parameterTypes.length > 1) {
        throw new UniauthCommonException("Not support api, parameter more than 1");
      }
      if (parameterTypes.length == 0) {
        paramClz = null;
      } else {
        paramClz = parameterTypes[0];
      }
    }

    @Override
    public String toString() {
      return "InvokedApi{" +
          "target=" + target +
          ", invokeMethod=" + invokeMethod +
          ", paramClz=" + paramClz +
          '}';
    }
  }
}
