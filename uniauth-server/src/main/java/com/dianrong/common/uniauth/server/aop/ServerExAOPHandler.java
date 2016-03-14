package com.dianrong.common.uniauth.server.aop;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.exp.AppException;
import com.dianrong.common.uniauth.server.track.GlobalVar;
import com.dianrong.common.uniauth.server.track.GlobalVarQueue;
import com.dianrong.common.uniauth.server.track.RequestManager;
import com.dianrong.common.uniauth.server.util.JasonUtil;

/**
 * Created by Arc on 15/1/16.
 */
@Aspect
@Component
public class ServerExAOPHandler {
    private static Logger logger = LoggerFactory.getLogger(ServerExAOPHandler.class);
    
    //todo: should migrate to zoo keeper
    private static final boolean IS_PRINT_STACKTRACE = true;

    @Autowired
    private GlobalVarQueue globalVarQueue;
    
    @Pointcut(value = "execution(public * com.dianrong.common.uniauth.server.resource.*.*(..))")
    public void anyServerResources() {
    }

    @Around("anyServerResources()")
    public Object handleException(ProceedingJoinPoint joinPoint) {
    	GlobalVar origin = RequestManager.getGlobalVar();
    	GlobalVar gv = null;
        try {
        	gv = (GlobalVar)origin.clone();
        	gv.setReqDate(new Date());
        	gv.setMapper(joinPoint.getSignature().getDeclaringType().getSimpleName());
        	String invokeMethod = joinPoint.getSignature().getName();
        	gv.setMethod(invokeMethod);
        	Long invokeSeq = gv.getInvokeSeq();
        	Long nextSeq =  invokeSeq + 1;
        	gv.setInvokeSeq(nextSeq);
        	origin.setInvokeSeq(nextSeq);
        	
        	try{
            	Object[] args = joinPoint.getArgs();
            	if(args != null && args.length > 0){
            		Object param = args[0];
            		if(param != null){
                		String jasonParam = JasonUtil.object2Jason(param);
                		gv.setReqParam(jasonParam);
                		Class<?> paramClazz = param.getClass();
                		Method getOpUserId = paramClazz.getMethod("getOpUserId", (Class[])null);
                		Long opUserId = (Long)getOpUserId.invoke(param, (Object[])null);
                		gv.setUserId(opUserId);
                		
                		Method getOpDomainId = paramClazz.getMethod("getOpDomainId", (Class[])null);
                		Integer opDomainId = (Integer)getOpDomainId.invoke(param, (Object[])null);
                		gv.setDomainId(opDomainId);
                		
                		origin.setUserId(opUserId);
                		origin.setDomainId(opDomainId);
            		}
            	}
        	}catch(Exception e){
        		logger.error("Can not get opUserid from request param.", e);
        	}
        	
        	long start = System.currentTimeMillis();
            Response<?> response = (Response<?>)joinPoint.proceed();
            long end = System.currentTimeMillis();
            long elapse = end - start;
            gv.setElapse(elapse);
            gv.setSuccess(AppConstants.ZERO_Byte);
            
            globalVarQueue.add(gv);
            return response;
        } catch(AppException appExp){
        	logger.error("appException occured", appExp);
            String expInfo = ExceptionUtils.getStackTrace(appExp);
            gv.setException(expInfo);
            globalVarQueue.add(gv);
            
        	return Response.failure(Info.build(appExp.getInfoName(), appExp.getMsg()));
        } catch (Throwable throwable) {
            logger.error("exception occured", throwable);
            String expInfo = ExceptionUtils.getStackTrace(throwable);
            gv.setException(expInfo);
            globalVarQueue.add(gv);
            
            if(IS_PRINT_STACKTRACE){
                List<Info> infos = new ArrayList<>();
                infos.add(Info.build(InfoName.STACKTRACE, expInfo));
                infos.add(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
                return Response.failure(infos);
            }
            else{
            	return Response.failure(Info.build(InfoName.INTERNAL_ERROR, "系统内部错误,请联系程序员"));
            }
        }
    }
    
//    public static void main(String args[]) {
//        String temp = "insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)" +
//        "values('200000004', '赵文乐', 'wenle.zhao@dianrong.com', '15026682113', 'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());";
//        String temp2 = "insert into user_grp(user_id, grp_id, type) values(200000001, 1, 1);";
//        List<String> sqls = new ArrayList<>();
//        String template = "insert into user(id, name, email, phone, password, password_salt, last_login_time, last_login_ip, fail_count, status, create_date, last_update, password_date)";
//        for(int i=1;i<3001;i++) {
//            int id = 210000004 + i;
//            String sql = template + "values('" + id + "', '"+"名字:"+id+"', '" + id + "@test.com', '" +id+"', " + "'GRodddDAZjK2tGZ6kT7ImP8ILwU=', 'I9JTzG2zzBAW3Q5NvP8lRg==', now(), '192.168.18.5', '0', '0', now(), now(), now());";
//            System.out.println(sql);
//            String sql2 = "insert into user_grp(user_id, grp_id, type) values(" + id + ", 1, 0);";
//            System.out.println(sql2);
//        }
//        //System.out.println(temp);
//    }
}
