package com.dianrong.common.uniauth.cas.service.support;

import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity;
import com.dianrong.common.uniauth.cas.service.support.annotation.TenancyIdentity.Type;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * for annotation TenancyIdentity. set tenancy identity
 *
 * @author wanglin
 */
@Aspect
@Component
@Slf4j
public class TenancyIdentityAop {

  @Pointcut(
      value = "@annotation(com.dianrong.common.uniauth"
          + ".cas.service.support.annotation.TenancyIdentity)")
  public void tenancyIdentitySet() {}

  /**
   * 用于处理注解@TenancyIdentity.
   * 
   * @see {@link TenancyIdentity}
   */
  @Around("tenancyIdentitySet()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    Object target = joinPoint.getTarget();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    Class<?>[] parameterTypes =
        ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterTypes();
    Method method = null;
    try {
      method = target.getClass().getMethod(methodName, parameterTypes);
      if (method.isBridge()) {
        for (int i = 0; i < args.length; i++) {
          Class<?> genClazz = ReflectionUtils.getSuperClassGenricType(target.getClass());
          if (args[i].getClass().isAssignableFrom(genClazz)) {
            parameterTypes[i] = genClazz;
          }
        }
        method = target.getClass().getMethod(methodName, parameterTypes);
      }
    } catch (NoSuchMethodException e) {
      log.error(target.getClass().getName() + " do not has method " + methodName, e);
    } catch (SecurityException e) {
      log.error(e.getMessage(), e);
    }
    if (method != null) {
      TenancyIdentity annotation = method.getAnnotation(TenancyIdentity.class);
      Type type = annotation.type();
      int index = annotation.index();
      CxfHeaderHolder holder = getCxfHeaderHolder(type);
      if (index >= args.length) {
        throw new IndexOutOfBoundsException(
            "please check TenancyIdentity annotaion index parameter, it is start from 0 ");
      }
      Object identity = args[index];
      holder.set(identity);
      log.debug("indentity is " + identity);
      try {
        return joinPoint.proceed(joinPoint.getArgs());
      } finally {
        holder.set(null);
      }
    } else {
      log.error(
          "because can not find the method, so do not set tenancy identity to CxfHeaderHolder");
      return joinPoint.proceed(joinPoint.getArgs());
    }
  }

  private CxfHeaderHolder getCxfHeaderHolder(Type type) {
    switch (type) {
      case CODE:
        return CxfHeaderHolder.TENANCYCODE;
      case ID:
        return CxfHeaderHolder.TENANCYID;
      default:
        throw new RuntimeException(type + " is not a valid Type");
    }
  }
}
