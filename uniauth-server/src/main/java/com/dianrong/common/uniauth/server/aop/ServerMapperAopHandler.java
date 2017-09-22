package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.support.audit.MapperAudit;
import com.dianrong.common.uniauth.server.support.audit.NoneMapperAudit;
import com.dianrong.common.uniauth.server.track.GlobalVar;
import com.dianrong.common.uniauth.server.track.GlobalVarQueueFacade;
import com.dianrong.common.uniauth.server.track.RequestManager;
import com.dianrong.common.uniauth.server.util.JasonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Aspect
@Component
@Slf4j
public class ServerMapperAopHandler {

  @Autowired
  private GlobalVarQueueFacade globalVarQueue;

  @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.data.mapper.*.*(..)) ")
  public void mappers() {
  }

  /**
   * 不进行Audit日志记录的方法列表.
   */
  private static final Set<String> NONE_AUDIT_MAPPER_METHODS = new HashSet<String>() {
    {
      add("countByExample");
      add("selectByExample");
      add("selectByPrimaryKey");
    }
  };

  /**
   * 记录日志Audit.
   */
  @Around("mappers()")
  public Object interceptMapper(ProceedingJoinPoint joinPoint) throws Throwable {
    GlobalVar origin = RequestManager.getGlobalVar();
    if (origin == null) {
      return joinPoint.proceed();
    }
    GlobalVar gv = (GlobalVar) origin.clone();
    gv.setReqDate(new Date());
    gv.setMapper(joinPoint.getSignature().getDeclaringType().getSimpleName());
    String invokeMethod = joinPoint.getSignature().getName();
    gv.setMethod(invokeMethod);
    gv.setReqUrl(null);
    origin.setInvokeSeq(gv.incrementInvokeSeq());
    try {
      Object[] args = joinPoint.getArgs();
      if (args != null && args.length > 0) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < args.length; i++) {
          Object obj = args[i];
          if (obj == null) {
            sb.append("#" + i + ":null ");
          } else {
            String jasonParam = JasonUtil.object2Jason(obj);
            sb.append("#" + i + ":" + jasonParam + " ");
          }
        }
        gv.setReqParam(sb.toString());
      }
    } catch (Exception e) {
      log.error("Can not get param when access mapper methods.", e);
    }
    try {
      long start = System.currentTimeMillis();
      final Object result = joinPoint.proceed();
      long end = System.currentTimeMillis();
      long elapse = end - start;
      gv.setElapse(elapse);
      gv.setSuccess(AppConstants.SUCCESS);
      if (auditLog(joinPoint)) {
        globalVarQueue.add(gv);
      } else {
        log.debug(gv.getMapper() + "." + invokeMethod + " consume millis :" + elapse);
        gv = null;
      }
      return result;
    } catch (Throwable throwable) {
      log.error("exception occured", throwable);
      String expInfo = ExceptionUtils.getStackTrace(throwable);
      gv.setSuccess(AppConstants.FAILURE);
      gv.setException(expInfo);
      globalVarQueue.add(gv);
      throw throwable;
    }
  }

  /**
   * 判断是否进行audit日志记录.
   */
  private boolean auditLog(ProceedingJoinPoint joinPoint) {
    Class<?>[] interfaces = joinPoint.getTarget().getClass().getInterfaces();
    if (interfaces == null || interfaces.length != 1) {
      return false;
    }
    // 此处只考虑mybatis自动生成的mapper是实现
    Class<?> destClz = interfaces[0];
    MapperAudit classAudit = destClz.getAnnotation(MapperAudit.class);
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Method destMethod = null;
    try {
      destMethod = destClz.getDeclaredMethod(method.getName(), method.getParameterTypes());
    } catch (NoSuchMethodException e) {
      log.debug(destClz.getName() + " has no method:" + method.getName());
    }
    if (destMethod == null) {
      return false;
    }
    MapperAudit methodAudit = destMethod.getAnnotation(MapperAudit.class);
    NoneMapperAudit methodNoneAudit = destMethod.getAnnotation(NoneMapperAudit.class);
    if (methodNoneAudit != null) {
      return false;
    }
    if (classAudit != null || methodAudit != null) {
      String methodName = joinPoint.getSignature().getName();
      return !NONE_AUDIT_MAPPER_METHODS.contains(methodName);
    }
    return false;
  }
}
