package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.Operator;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.service.TenancyService;
import com.dianrong.common.uniauth.server.support.apicontrl.CallerAccountHolder;
import com.dianrong.common.uniauth.server.track.GlobalVar;
import com.dianrong.common.uniauth.server.track.GlobalVarQueueFacade;
import com.dianrong.common.uniauth.server.track.RequestManager;
import com.dianrong.common.uniauth.server.util.JasonUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by Arc on 15/1/16.
 */
@Aspect
@Component
@Slf4j
public class ServerExAopHandler {

  // todo: should migrate to zoo keeper
  private static final boolean IS_PRINT_STACKTRACE = true;

  @Autowired
  private GlobalVarQueueFacade globalVarQueue;

  @Autowired
  private TenancyService tenancyService;

  @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
  public void anyServerResources() {
  }

  /**
   * 统一处理系统异常信息.
   */
  @Around("anyServerResources()")
  public Object handleException(ProceedingJoinPoint joinPoint) throws Throwable {
    GlobalVar origin = RequestManager.getGlobalVar();
    if (origin == null) {
      return joinPoint.proceed();
    }
    Long tenancyId = tenancyService.getOneCanUsedTenancyId();
    origin.setTenancyId(tenancyId);
    GlobalVar gv = null;
    try {
      origin.setRequestDomainCode(CallerAccountHolder.get());
      gv = (GlobalVar) origin.clone();
      gv.setReqDate(new Date());
      gv.setMapper(joinPoint.getSignature().getDeclaringType().getSimpleName());
      String invokeMethod = joinPoint.getSignature().getName();
      gv.setMethod(invokeMethod);
      Long invokeSeq = gv.getInvokeSeq();
      Long nextSeq = invokeSeq + 1;
      gv.setInvokeSeq(nextSeq);
      origin.setInvokeSeq(nextSeq);
      try {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0) {
          Object param = args[0];
          if (param != null) {
            String jasonParam = JasonUtil.object2Jason(param);
            gv.setReqParam(jasonParam);
            if (param instanceof Operator) {
              Operator operatorParam = (Operator)param;
              gv.setUserId(operatorParam.getOpUserId());
              gv.setDomainId(operatorParam.getOpDomainId());

              origin.setUserId(operatorParam.getOpUserId());
              origin.setDomainId(operatorParam.getOpDomainId());
            }
          }
        }
      } catch (Exception e) {
        log.error("Can not get opUserid from request param.", e);
      }

      long start = System.currentTimeMillis();
      final Object response = joinPoint.proceed();
      long end = System.currentTimeMillis();
      long elapse = end - start;
      gv.setElapse(elapse);
      gv.setSuccess(AppConstants.ZERO_BYTE);

      globalVarQueue.add(gv);
      return response;
    } catch (AppException appExp) {
      log.error("appException occured", appExp);
      String expInfo = ExceptionUtils.getStackTrace(appExp);
      gv.setException(expInfo);
      globalVarQueue.add(gv);

      return Response.failure(Info.build(appExp.getInfoName(), appExp.getMsg()));
    } catch (Throwable throwable) {
      log.error("exception occured", throwable);
      String expInfo = ExceptionUtils.getStackTrace(throwable);
      gv.setException(expInfo);
      globalVarQueue.add(gv);

      if (IS_PRINT_STACKTRACE) {
        List<Info> infos = new ArrayList<>();
        infos.add(Info.build(InfoName.STACKTRACE, expInfo));
        infos.add(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
        return Response.failure(infos);
      } else {
        return Response.failure(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
      }
    }
  }

}
