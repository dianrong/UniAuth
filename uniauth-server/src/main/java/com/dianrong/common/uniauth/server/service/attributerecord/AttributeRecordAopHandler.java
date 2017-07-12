package com.dianrong.common.uniauth.server.service.attributerecord;

import com.dianrong.common.uniauth.server.data.entity.AttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.ExtendVal;
import com.dianrong.common.uniauth.server.data.entity.GrpAttributeRecords;
import com.dianrong.common.uniauth.server.data.entity.UserAttributeRecords;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;
import com.dianrong.common.uniauth.server.service.attributerecord.handler.AttributeRecordHandler;
import com.dianrong.common.uniauth.server.service.inner.GrpAttributeRecordsInnerService;
import com.dianrong.common.uniauth.server.service.inner.UserAttributeRecordsInnerService;
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
import org.springframework.beans.factory.annotation.Autowired;
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
      value = "@annotation(com.dianrong.common.uniauth"
          + ".server.service.attributerecord.ExtendAttributeRecord)")
  public void attributeRecord() {}

  /**
   * 通过工厂获取对应的处理handler.
   */
  @Autowired
  private AttributeRecordHanlderFactory attributeRecordHanlderFactory;

  @Autowired
  private UserAttributeRecordsInnerService userAttributeRecordsInnerService;

  @Autowired
  private GrpAttributeRecordsInnerService grpAttributeRecordsInnerService;

  /**
   * 记录属性扩展操作记录.
   */
  @Around("attributeRecord()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    AttributeValIdentity valIdentity = getValIndentity(joinPoint);
    TypeOperate typeOperate = getTypeOperate(joinPoint);
    AttributeRecordHandler handler = attributeRecordHanlderFactory.getHandler(typeOperate);
    boolean parameterCheckOk = true;
    ExtendVal originalVal = null;
    try {
      originalVal = handler.invokeTargetBefore(valIdentity);
    } catch (InvalidParameterTypeException ite) {
      parameterCheckOk = false;
      log.error(String.format(
          "AttributeRecordHandler invoke target before parameter"
          + " is invalid.TypeOperate:%s, Identity:%s",
          typeOperate, valIdentity), ite);
    }
    Throwable throwable = null;
    try {
      return joinPoint.proceed();
    } catch (Throwable t) {
      log.error("invoke failed!", t);
      throwable = t;
      throw t;
    } finally {
      if (parameterCheckOk) {
        AttributeRecords attributeRecord =
            handler.invokeTargetAfter(valIdentity, originalVal, throwable);
        if (attributeRecord != null) {
          // 目前暂时需要同步提交数据,后期再修改为其他方式提交效率.
          // 因为通过异步有一个问题,如果整个大事务操作Rollback了,但是记录却错误的记录进去了
          insertRecords(attributeRecord);
        }
      }
    }
  }

  private void insertRecords(AttributeRecords record) {
    if (record instanceof UserAttributeRecords) {
      userAttributeRecordsInnerService.insert((UserAttributeRecords) record);
      log.debug("success insert {}", record);
      return;
    }
    if (record instanceof GrpAttributeRecords) {
      grpAttributeRecordsInnerService.insert((GrpAttributeRecords) record);
      log.debug("success insert {}", record);
      return;
    }
    log.warn("{} is not supported to insert, so just ignored!", record);
  }

  private TypeOperate getTypeOperate(ProceedingJoinPoint joinPoint) {
    Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
    ExtendAttributeRecord extendAttributeRecord =
        targetMethod.getAnnotation(ExtendAttributeRecord.class);
    TypeOperate typeOperate = new TypeOperate();
    typeOperate.setOperate(extendAttributeRecord.operate());
    typeOperate.setType(extendAttributeRecord.type());
    return typeOperate;
  }

  /**
   * 通过分析注解获取能唯一标识扩展属性值记录的标识.
   */
  private AttributeValIdentity getValIndentity(ProceedingJoinPoint joinPoint) {
    AttributeValIdentity valIdentity = new AttributeValIdentity();
    Method targetMethod = ((MethodSignature) joinPoint.getSignature()).getMethod();
    ExtendAttributeRecord extendAttributeRecord =
        targetMethod.getAnnotation(ExtendAttributeRecord.class);
    Object[] args = joinPoint.getArgs();

    // get primary key
    Long primaryId = null;
    String primaryIdExp = extendAttributeRecord.primaryId();
    if (!StringUtils.isBlank(primaryIdExp)) {
      // 直接转换,如果有错误直接抛出取
      primaryId = Long.valueOf(getElExpress(joinPoint, primaryIdExp).toString());
    } else {
      int primaryIdIndex = extendAttributeRecord.primaryIdIndex();
      if (primaryIdIndex >= 0) {
        if (primaryIdIndex >= args.length) {
          log.error("ExtendAttributeRecord primary index param is error! {}", primaryIdIndex);
        }
        primaryId = Long.valueOf(args[primaryIdIndex].toString());
      }
    }
    if (primaryId != null) {
      valIdentity.setPrimaryId(primaryId);
      return valIdentity;
    }

    // 如果没有指定主键id,继续通过identity和extendId去匹配.

    // Identity
    Object identity;
    String identityExp = extendAttributeRecord.identity();
    if (!StringUtils.isBlank(identityExp)) {
      identity = getElExpress(joinPoint, identityExp);
    } else {
      int identityIndex = extendAttributeRecord.identityIndex();
      if (identityIndex >= args.length) {
        log.error("ExtendAttributeRecord identity index param is error! {}", identityIndex);
      }
      identity = args[identityIndex];
    }
    valIdentity.setIdentity(identity);

    // ExtendId
    Long extendId;
    String extendIdExp = extendAttributeRecord.extendId();
    if (!StringUtils.isBlank(extendIdExp)) {
      extendId = Long.valueOf(getElExpress(joinPoint, extendIdExp).toString());
    } else {
      int extendIdIndex = extendAttributeRecord.extendIdIndex();
      if (extendIdIndex >= args.length) {
        log.error("ExtendAttributeRecord extendId index param is error! {}", extendIdIndex);
      }
      extendId = Long.valueOf(args[extendIdIndex].toString());
    }
    valIdentity.setExtendId(extendId);
    return valIdentity;
  }

  private Object getElExpress(ProceedingJoinPoint joinPoint, String expression) {
    Object[] args = joinPoint.getArgs();
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Expression exp = getExp(expression);
    MethodBasedEvaluationContext evaluationContext =
        new MethodBasedEvaluationContext(null, method, args, this.paramNameDiscoverer);
    return exp.getValue(evaluationContext);
  }

  private Expression getExp(String expression) {
    Expression exp = cache.get(expression);
    if (exp != null) {
      return exp;
    }
    exp = parser.parseExpression(expression);
    cache.putIfAbsent(expression, exp);
    return cache.get(expression);
  }
}
