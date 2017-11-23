package com.dianrong.common.techops.service;

import com.dianrong.common.techops.exp.InvalidParameterException;
import com.dianrong.common.techops.exp.NoAuthorityException;
import com.dianrong.common.techops.exp.NotFoundApiException;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 处理uniauth-server的api代理处理.
 */

@Service
public class ProxyInvokeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyInvokeService.class);

  @Resource
  private UARWFacade uarwFacade;

  private Set<InvokeKey> allApiPath = new HashSet<>();
  private Map<InvokeKey, InvokedApi> readApiMap = new HashMap<>();
  private Map<InvokeKey, InvokedApi> writeApiMap = new HashMap<>();

  /**
   * 初始化方法，在使用之前需要调用.
   */
  @PostConstruct
  public void init() {
    LOGGER.info("Start collect uniauth server api list.");
    Field[] fieldArray = uarwFacade.getClass().getDeclaredFields();
    for (Field field : fieldArray) {
      Class<?> clz = field.getType();
      String clzName = clz.getSimpleName();
      if (clz.isInterface() && clzName.startsWith("I") && clzName.endsWith("Resource")) {
        parseApiMap(field);
      }
    }
    LOGGER.info("Finish collect uniauth server api list.ReadApiMap:" + readApiMap + ", WriteApiMap:"
        + writeApiMap);
  }

  /**
   * 调用Uniauth-server的Api.
   *
   * @param paramStr 请求json参数.
   * @param path 请求的路径.
   * @param method 请求方法.
   * @return 接口返回结果.
   * @throws NotFoundApiException 请求的方法不存在.
   * @throws NoAuthorityException 没有权限访问.
   * @throws InvalidParameterException 请求参数不对.
   */
  public Object invokeApi(String paramStr, String path, String method)
      throws NotFoundApiException, NoAuthorityException, InvalidParameterException {
    InvokeKey key = new InvokeKey(method, path);
    if (!allApiPath.contains(key)) {
      throw new NotFoundApiException("No api match:" + key);
    }
    InvokedApi invokedApi = readApiMap.get(key);
    if (!currentRequestIsFromSuperAdmin()) {
      if (invokedApi == null) {
        throw new NoAuthorityException("No authority to invoke api:" + key);
      }
    } else {
      // 超级管理员可以调用写接口.
      if (invokedApi == null) {
        invokedApi = writeApiMap.get(key);
        if (invokedApi == null) {
          throw new NoAuthorityException("No authority to invoke api:" + key);
        }
      }
    }

    Class<?> paramClz = invokedApi.paramClz;
    Object[] parameters;
    if (paramClz != null) {
      Object param;
      try {
        param = JsonUtil.jsonToObject(paramStr, paramClz);
      } catch (RuntimeException e) {
        LOGGER.error(paramStr + " can not be cast to " + paramClz.getName(), e);
        throw new InvalidParameterException(paramStr + " can not be cast to " + paramClz.getName());
      }
      parameters = new Object[]{param};
    } else {
      // empty parameter list
      parameters = new Object[0];
    }
    try {
      return invokedApi.invokeMethod.invoke(invokedApi.target, parameters);
    } catch (Exception e) {
      LOGGER.error("Failed call api:" + key, e);
      return "Failed call api:" + key;
    }
  }

  /**
   * 判断当前请求用户是否是超级管理员.
   */
  private boolean currentRequestIsFromSuperAdmin() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      return false;
    }
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if (authority.getAuthority().equalsIgnoreCase(AppConstants.ROLE_SUPER_ADMIN)) {
        return true;
      }
    }
    return false;
  }


  private void parseApiMap(Field field) {
    Class<?> clz = field.getType();
    String clzName = clz.getSimpleName();
    Class<?> readClz = null;
    Class<?> writeClz = null;
    if (clzName.contains("RW")) {
      writeClz = clz;
      Class<?>[] interfaces = writeClz.getInterfaces();
      for (Class<?> superInterface : interfaces) {
        String interfaceClzName = superInterface.getSimpleName();
        if (superInterface.isInterface() && interfaceClzName.startsWith("I") && interfaceClzName
            .endsWith("Resource")) {
          readClz = superInterface;
          break;
        }
      }
    } else {
      readClz = clz;
    }
    if (readClz == null) {
      throw new UniauthCommonException(field.getName() + " is invalid!");
    }
    Path path = readClz.getAnnotation(Path.class);
    if (path == null) {
      throw new UniauthCommonException(field.getName() + " is invalid!");
    }
    String basePath = path.value();
    basePath = basePath == null ? "" : basePath.trim();
    parseApi(basePath, field, readClz, readApiMap);
    if (writeClz != null) {
      parseApi(basePath, field, writeClz, writeApiMap);
    }
  }

  /**
   * 将每个接口的定义方法解析,然后放入对应的apiMap中(分为读和写).
   */
  private void parseApi(String basePath, Field field, Class<?> clz,
      Map<InvokeKey, InvokedApi> apiMap) {
    Method[] methodList = clz.getDeclaredMethods();
    if (methodList == null || methodList.length == 0) {
      return;
    }
    for (Method method : methodList) {
      Path path = method.getAnnotation(Path.class);
      String pathStr = path.value();
      String httpMethod = getHttpMethod(method);
      Object target;
      try {
        field.setAccessible(true);
        target = field.get(uarwFacade);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        LOGGER.error("Failed get field:" + field.getName());
        throw new UniauthCommonException("Failed get field:" + field.getName(), e);
      }
      InvokeKey key = new InvokeKey(httpMethod, basePath + "/" + pathStr);
      InvokedApi api = new InvokedApi(target, method);
      apiMap.put(key, api);
      allApiPath.add(key);
    }
  }

  private String getHttpMethod(Method method) {
    POST post = method.getAnnotation(POST.class);
    if (post != null) {
      return POST.class.getSimpleName();
    }
    GET get = method.getAnnotation(GET.class);
    if (get != null) {
      return GET.class.getSimpleName();
    }
    PUT put = method.getAnnotation(PUT.class);
    if (put != null) {
      return PUT.class.getSimpleName();
    }
    HEAD head = method.getAnnotation(HEAD.class);
    if (head != null) {
      return HEAD.class.getSimpleName();
    }
    DELETE delete = method.getAnnotation(DELETE.class);
    if (delete != null) {
      return DELETE.class.getSimpleName();
    }
    OPTIONS options = method.getAnnotation(OPTIONS.class);
    if (options != null) {
      return OPTIONS.class.getSimpleName();
    }
    throw new UniauthCommonException("Invalid method:" + method.getName());
  }

  /**
   * 将指定路径中重复的'/'去掉，开头的'/'也去掉.
   *
   * @param path 目标字符.
   * @return 如果传入字符串为空，则直接返回.
   */
  private String clearPath(String path) {
    if (!StringUtils.hasText(path)) {
      return path;
    }
    StringBuilder sb = new StringBuilder();
    boolean isTargetChar = false;
    for (char c : path.toCharArray()) {
      if (c != '/') {
        sb.append(c);
        isTargetChar = false;
        continue;
      }
      if (isTargetChar) {
        // 过滤掉重复字符串
        continue;
      } else {
        isTargetChar = true;
        sb.append(c);
      }
    }
    String clearPath = sb.toString();
    if (clearPath.startsWith("/")) {
      clearPath = clearPath.substring(1);
    }
    return clearPath;
  }

  private class InvokeKey {

    private final String method;
    private final String path;

    InvokeKey(String method, String path) {
      Assert.notNull(method);
      Assert.notNull(path);
      this.method = method.toUpperCase();
      this.path = clearPath(path);
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
