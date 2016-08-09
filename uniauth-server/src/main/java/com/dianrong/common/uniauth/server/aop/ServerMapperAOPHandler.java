package com.dianrong.common.uniauth.server.aop;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.track.GlobalVar;
import com.dianrong.common.uniauth.server.track.GlobalVarQueue;
import com.dianrong.common.uniauth.server.track.RequestManager;
import com.dianrong.common.uniauth.server.util.JasonUtil;
import com.dianrong.common.uniauth.server.util.RegExpUtil;

@Aspect
@Component
public class ServerMapperAOPHandler {
	private static Logger logger = LoggerFactory.getLogger(ServerMapperAOPHandler.class);
	

    @Autowired
    private GlobalVarQueue globalVarQueue;
    
    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.data.mapper.*.insert*(..)) ")
    public void insertMappers() {}
    
    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.data.mapper.*.update*(..)) ")
    public void updateMappers() {}
    
    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.data.mapper.*.delete*(..)) ")
    public void deleteMappers() {}

    @Pointcut(value = "!execution(public * com.dianrong.common.uniauth.server.data.mapper.AuditMapper.*(..))")
    public void notAuditMapper(){}
    
    @Around("notAuditMapper() && (insertMappers() || updateMappers() || deleteMappers())")
    public Object interceptMapper(ProceedingJoinPoint joinPoint) throws Throwable {
    	GlobalVar origin = RequestManager.getGlobalVar();
    	if(origin == null) return joinPoint.proceed();
    	GlobalVar gv = null;
        try {
        	gv = (GlobalVar)origin.clone();
        	gv.setReqDate(new Date());
        	gv.setMapper(joinPoint.getSignature().getDeclaringType().getSimpleName());
        	String invokeMethod = joinPoint.getSignature().getName();
        	gv.setMethod(invokeMethod);
        	gv.setReqUrl(null);
        	Long invokeSeq = gv.getInvokeSeq();
        	Long nextSeq =  invokeSeq + 1;
        	gv.setInvokeSeq(nextSeq);
        	origin.setInvokeSeq(nextSeq);
        	try{
            	Object[] args = joinPoint.getArgs();
            	if(args != null && args.length > 0){
            		StringBuffer sb = new StringBuffer();
            		for(int i = 0;i < args.length;i++){
            			Object obj = args[i];
            			if(obj == null){
            				sb.append("#" + i + ":null ");
            			}
            			else{
            				String jasonParam = JasonUtil.object2Jason(obj);
            				sb.append("#" + i + ":" + jasonParam + " ");
            			}
            		}
            		gv.setReqParam(sb.toString());
            	}
        	}catch(Exception e){
        		logger.error("Can not get param when access mapper methods.", e);
        	}
        	
        	long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            long elapse = end - start;
            gv.setElapse(elapse);
            gv.setSuccess(AppConstants.ZERO_Byte);
            
            globalVarQueue.add(gv);
            return result;
        } catch (Throwable throwable) {
            logger.error("exception occured", throwable);
            String expInfo = ExceptionUtils.getStackTrace(throwable);
            gv.setException(expInfo);
            globalVarQueue.add(gv);
            throw throwable;
        }
    }
}
