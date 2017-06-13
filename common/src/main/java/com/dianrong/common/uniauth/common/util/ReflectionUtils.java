package com.dianrong.common.uniauth.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionUtils {

  private ReflectionUtils() {}

  /**
   * 反射获取对象的属性值.
   */
  public static Object getField(Object targetObj, String fieldName, boolean isParentField) {
    Object object = null;
    try {
      Class<?> targetClazz = targetObj.getClass();
      Field field = null;
      if (isParentField) {
        field = targetClazz.getSuperclass().getDeclaredField(fieldName);
      } else {
        field = targetClazz.getDeclaredField(fieldName);
      }
      field.setAccessible(true);
      object = field.get(targetObj);
    } catch (Exception e) {
      log.warn("exception", e);
    }
    return object;
  }

  /**
   * 反射获取对象的属性值.
   */
  public static Object getField(Object targetObj, String fieldName) {
    Object object = null;
    try {
      Class<?> targetClazz = targetObj.getClass();
      Field field = null;
      while (targetClazz != null) {
        try {
          field = targetClazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException ex) {
          log.debug(targetClazz.getName() + " can not find field " + fieldName, ex);
        }
        // find it
        if (field != null) {
          break;
        }
        targetClazz = targetClazz.getSuperclass();
      }
      if (field == null) {
        throw new NoSuchFieldException(fieldName);
      }
      field.setAccessible(true);
      object = field.get(targetObj);
    } catch (Exception e) {
      log.warn("exception", e);
    }
    return object;
  }

  /**
   * 反射调用对象的方法.
   */
  public static Object invokeStaticMethodWithoutParam(Class<?> clazz, String methodName) {
    Object object = null;
    try {
      Method method = clazz.getMethod(methodName, new Class[0]);
      object = method.invoke(null, new Object[0]);
    } catch (Exception e) {
      log.warn("exception", e);
    }
    return object;
  }

  /**
   * 反射调用对象的方法.
   */
  public static Object invokeMethodWithoutParam(Object targetObj, String methodName) {
    Object object = null;
    try {
      Method method = targetObj.getClass().getMethod(methodName, new Class[0]);
      object = method.invoke(targetObj, new Object[0]);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return object;
  }

  /**
   * 反射调用对象的方法.
   */
  public static void setUserInfoField(Object targetObj, String fieldName, Object fieldValue) {
    Field field = null;
    Class<?> selfClazz = targetObj.getClass();
    while (field == null) {
      try {
        field = selfClazz.getDeclaredField(fieldName);
      } catch (Exception e) {
        log.debug("exception", e);
        selfClazz = selfClazz.getSuperclass();
      }
    }
    field.setAccessible(true);
    try {
      field.set(targetObj, fieldValue);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  /**
   * 给类设置静态属性值.
   */
  public static void setStaticField(String clazzName, String fieldName, Object fieldValue) {
    try {
      Class<?> clazz = Class.forName(clazzName);
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(null, fieldValue);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
  }

  /**
   * 给类设置静态属性值.
   */
  public static Object getStaticField(String clazzName, String fieldName) {
    Object object = null;
    try {
      Class<?> clazz = Class.forName(clazzName);
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      object = field.get(null);
    } catch (Exception e) {
      log.warn(e.getMessage());
    }
    return object;
  }

  /**
   * 获取操作人用户id.
   */
  public static Long getOpUserId() {
    Long opUserId = null;
    // SecurityContextHolder.getContext().getAuthentication().getPrincipal()
    try {
      Class<?> clazz =
          Class.forName("org.springframework.security.core.context.SecurityContextHolder");
      if (clazz != null) {
        Object securityContext =
            ReflectionUtils.invokeStaticMethodWithoutParam(clazz, "getContext");
        if (securityContext != null) {
          Object authentication =
              ReflectionUtils.invokeMethodWithoutParam(securityContext, "getAuthentication");
          if (authentication != null) {
            Object principal =
                ReflectionUtils.invokeMethodWithoutParam(authentication, "getPrincipal");
            if (principal != null) {
              opUserId = (Long) ReflectionUtils.invokeMethodWithoutParam(principal, "getId");
            }
          }
        }
      }
    } catch (ClassNotFoundException e) {
      log.debug(
          "class <org.springframework.security.core.context.SecurityContextHolder> not found, "
          + "maybe getOpUserId() called on the uniauth-server side?");
    }
    return opUserId;
  }

  /**
   * 获取超类的泛型类型.
   */
  public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
    Type genType = clazz.getGenericSuperclass();// 得到泛型父类
    // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
    if (!(genType instanceof ParameterizedType)) {
      return Object.class;
    }
    // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
    // DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
    Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
    if (index >= params.length || index < 0) {
      throw new RuntimeException("你输入的索引" + (index < 0 ? "不能小于0" : "超出了参数的总数"));
    }
    if (!(params[index] instanceof Class)) {
      return Object.class;
    }
    return (Class<?>) params[index];
  }

  /**
   * 通过反射,获得指定类的父类的第一个泛型参数的实际类型.
   * @param clazz clazz 需要反射的类,该类必须继承泛型父类
   * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回 <code>Object.class</code>
   */
  public static Class<?> getSuperClassGenricType(Class<?> clazz) {
    return getSuperClassGenricType(clazz, 0);
  }
}
