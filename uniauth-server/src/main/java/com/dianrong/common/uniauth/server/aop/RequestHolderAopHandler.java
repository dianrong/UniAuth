package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.common.bean.request.TenancyBasedParam;
import com.dianrong.common.uniauth.common.server.cxf.CxfHeaderHolder;
import com.dianrong.common.uniauth.server.support.tree.TreeType;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeHolder;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeTag;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 处理各种holder信息.
 */
@Aspect
@Component
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestHolderAopHandler {

  @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
  public void anyServerResources() {
  }

  /**
   * 处理任何的请求.
   */
  @Around("anyServerResources()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    holderSet(joinPoint);
    return joinPoint.proceed();
  }

  private void holderSet(ProceedingJoinPoint joinPoint) {
    // 处理租户标识信息
    processTenancyIdentity(joinPoint);
    // 处理树类型信息
    processTreeTypeTag(joinPoint);
  }

  /**
   * 处理租户的标识信息.
   */
  private void processTenancyIdentity(ProceedingJoinPoint joinPoint) {
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
            logMessage.append("set tenancyId manually: " + tp.getTenancyId());
            log.info(logMessage.toString());
            CxfHeaderHolder.TENANCYID.set(tp.getTenancyId());
          }
          if (tp.getTenancyCode() != null) {
            logMessage.append("set tenancyCode manually: " + tp.getTenancyCode());
            log.info(logMessage.toString());
            CxfHeaderHolder.TENANCYCODE.set(tp.getTenancyCode());
          }
        }
      }
    }
  }

  /**
   * 处理树的类型标识信息.
   */
  private void processTreeTypeTag(ProceedingJoinPoint joinPoint) {
    // 处理树结构标识信息
    Class<?> tartClz = joinPoint.getTarget().getClass();
    TreeTypeTag treeTypeTag = tartClz.getAnnotation(TreeTypeTag.class);
    if (treeTypeTag != null) {
      TreeType type = treeTypeTag.value();
      if (type != null) {
        TreeTypeHolder.set(type);
      }
    }
  }
}
