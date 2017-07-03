package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.server.service.support.ExtendAttributeRecord;
import com.google.common.collect.Maps;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

/**
 * 用于处理扩展属性值的记录问题.
 */
@Slf4j
@Aspect
@Component
public class AttributeRecordAopHandler {

  private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();

  private final SpelExpressionParser parser = new SpelExpressionParser();

  private final ConcurrentMap<String, Expression> cache = Maps.newConcurrentMap();

  @Pointcut(
      value = "@annotation(com.dianrong.common.uniauth.server.service.support.ExtendAttributeRecord)")
  public void attributeRecord() {}

  /**
   * 记录属性扩展操作记录.
   */
  @Around("attributeRecord()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    Object identity = getIndentity(joinPoint);
    Object extendId = getExtendId(joinPoint);
    return joinPoint.proceed();
  }

  private Object getIndentity(ProceedingJoinPoint joinPoint) {
    // get expression
    ExtendAttributeRecord extendAttributeRecord = joinPoint.getTarget().getClass().getAnnotation(ExtendAttributeRecord.class);
    String expression = extendAttributeRecord.identity();
    if (!StringUtils.isBlank(expression)) {
      return getElExpress(joinPoint, expression);
    }
    Object[] args = joinPoint.getArgs();
    int identityIndex = extendAttributeRecord.identityIndex();
    if (identityIndex >= args.length) {
      log.error("ExtendAttributeRecord identity index param is error! {}", identityIndex);
    }
    return args[identityIndex];
  }
  
  private Object getExtendId(ProceedingJoinPoint joinPoint) {
    ExtendAttributeRecord extendAttributeRecord = joinPoint.getTarget().getClass().getAnnotation(ExtendAttributeRecord.class);
    String expression = extendAttributeRecord.extendId();
    if (!StringUtils.isBlank(expression)) {
      return getElExpress(joinPoint, expression);
    }
    Object[] args = joinPoint.getArgs();
    int identityIndex = extendAttributeRecord.extendIdIndex();
    if (identityIndex >= args.length) {
      log.error("ExtendAttributeRecord extendId index param is error! {}", identityIndex);
    }
    return args[identityIndex];
  }
  
  private Object getElExpress(ProceedingJoinPoint joinPoint, String expression) {
    Object[] args = joinPoint.getArgs();
    Method method = ((MethodSignature)joinPoint.getSignature()).getMethod();
    Expression exp = getExp(expression);
    MethodBasedEvaluationContext evaluationContext = new MethodBasedEvaluationContext(null,
        method, args, this.paramNameDiscoverer);
    return exp.getValue(evaluationContext);
  }
  
  private Expression getExp(String expression) {
    Expression exp = cache.get(expression);
    if (exp != null) {
      return exp;
    }
    exp = parser.parseExpression("hello");
    cache.putIfAbsent(expression, exp);
    return cache.get(expression);
  }
}
