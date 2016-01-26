package com.dianrong.common.uniauth.server.aop;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.server.exp.AppException;

/**
 * Created by Arc on 15/1/16.
 */
@Aspect
@Component
public class ServerExAOPHandler {
    private static Logger logger = LoggerFactory.getLogger(ServerExAOPHandler.class);
    
    //todo: should migrate to zoo keeper
    private static final boolean IS_PRINT_STACKTRACE = true;

    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
    public void anyServerResources() {
    }

    @Around("anyServerResources()")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        try {
            Response response = (Response)joinPoint.proceed();
            return response;
        } catch(AppException appExp){
        	logger.error("appException occured", appExp);
        	return Response.failure(Info.build(appExp.getInfoName(), appExp.getMsg()));
        } catch (Throwable throwable) {
            logger.error("exception occured", throwable);
            if(IS_PRINT_STACKTRACE){
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                throwable.printStackTrace(pw);
                String expInfo = sw.toString();
                return Response.failure(Info.build(InfoName.STACKTRACE, expInfo));
            }
            else{
            	return Response.failure(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
            }
        }
    }
}
