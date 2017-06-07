package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.common.bean.request.TenancyBasedParam;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * Created by Arc on 15/1/16.
 */
@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdentityAopHandler {

  @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
  public void anyServerResources() {
  }

  /**
   * 处理任何的请求.
   */
  @Around("anyServerResources()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    Object[] args = joinPoint.getArgs();
    if (args != null && args.length > 0) {
      StringBuilder logMessage = new StringBuilder();
      String requestMethod = joinPoint.getSignature().getName();
      logMessage.append("request Method: " + requestMethod + ",");
      for (int i = args.length - 1; i >= 0; i--) {
        // 从后到前覆盖
        Object param = args[i];

        // tenancy identity
        if (param instanceof TenancyBasedParam) {
          TenancyBasedParam tp = (TenancyBasedParam) param;
          if (tp.getTenancyId() != null) {
            logMessage.append("set tenancyId manully: " + tp.getTenancyId());
            log.info(logMessage.toString());
            CxfHeaderHolder.TENANCYID.set(tp.getTenancyId());
          }
          if (tp.getTenancyCode() != null) {
            logMessage.append("set tenancyCode manully: " + tp.getTenancyCode());
            log.info(logMessage.toString());
            CxfHeaderHolder.TENANCYCODE.set(tp.getTenancyCode());
          }
        }
      }
    }
    return joinPoint.proceed();
  }
}
