package com.dianrong.common.uniauth.server.aop;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;

/**
 * Created by Arc on 15/1/16.
 */
@Aspect
@Component
public class ServerExAOPHandler {
    private static Logger logger = LoggerFactory.getLogger(ServerExAOPHandler.class);

    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
    public void anyServerResources() {
    }

    @Around("anyServerResources()")
    public Object handleException(ProceedingJoinPoint joinPoint) {
        try {
            Response response = (Response)joinPoint.proceed();
            return response;
        } catch (Throwable throwable) {
            logger.error("exception occured", throwable);
            return Response.failure(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
        }
    }
}
