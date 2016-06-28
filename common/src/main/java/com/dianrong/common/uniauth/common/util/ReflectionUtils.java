package com.dianrong.common.uniauth.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectionUtils {

	/**./**.
	 * 日志对象
	 */
	private final static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
	
	private ReflectionUtils() {
	}

	public static Object getField(Object targetObj, String fieldName, boolean isParentField){
		Object object = null;
		try{
			Class<?> targetClazz = targetObj.getClass();
			Field field = null;
			if(isParentField){
				field = targetClazz.getSuperclass().getDeclaredField(fieldName);
			}
			else{
				field = targetClazz.getDeclaredField(fieldName);
			}
			field.setAccessible(true);
			object = field.get(targetObj);
		}catch(Exception e){
			logger.warn("exception", e);
		}
		return object;
	}
	
	public static Object invokeStaticMethodWithoutParam(Class<?> clazz, String methodName){
		Object object = null;
		try{
			Method method = clazz.getMethod(methodName, new Class[0]);
			object = method.invoke(null, new Object[0]);
		}catch(Exception e){
			logger.warn("exception", e);
		}
		return object;
	}
	
	public static Object invokeMethodWithoutParam(Object targetObj, String methodName){
		Object object = null;
		try{
			Method method = targetObj.getClass().getMethod(methodName, new Class[0]);
			object = method.invoke(targetObj, new Object[0]);
		}catch(Exception e){
			logger.warn("exception", e);
		}
		return object;
	}
	
	public static void setUserInfoField(Object targetObj, String fieldName, Object fieldValue){
		Field field = null;
		Class<?> selfClazz = targetObj.getClass();
		while(field == null){
			try{
				field = selfClazz.getDeclaredField(fieldName);
			}catch(Exception e){
				logger.warn("exception", e);
				selfClazz = selfClazz.getSuperclass();
			}
		}
		field.setAccessible(true);
		try{
			field.set(targetObj, fieldValue);
		}catch(Exception e){
			logger.warn("exception", e);
		}
	}
	
	public static void setStaticField(String clazzName, String fieldName, Object fieldValue){
		try{
			Class<?> clazz = Class.forName(clazzName);
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(null, fieldValue);
		}catch(Exception e){
			logger.warn("exception", e);
		}
	}
	
	public static Object getStaticField(String clazzName, String fieldName){
		Object object = null;
		try{
			Class<?> clazz = Class.forName(clazzName);
			Field field = clazz.getDeclaredField(fieldName);
			field.setAccessible(true);
			object = field.get(null);
		}catch(Exception e){
			logger.warn("exception", e);
		}
		return object;
	}

	public static Long getOpUserId() {
		Long opUserId = null;
		// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
		try {
			Class<?> clazz = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
			if (clazz != null) {
				Object securityContext = ReflectionUtils.invokeStaticMethodWithoutParam(clazz, "getContext");
				if (securityContext != null) {
					Object authentication = ReflectionUtils.invokeMethodWithoutParam(securityContext, "getAuthentication");
					if (authentication != null) {
						Object principal = ReflectionUtils.invokeMethodWithoutParam(authentication, "getPrincipal");
						if (principal != null) {
							opUserId = (Long) ReflectionUtils.invokeMethodWithoutParam(principal, "getId");
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			logger.debug("class <org.springframework.security.core.context.SecurityContextHolder> not found, maybe getOpUserId() called on the uniauth-server side?");
		}
		return opUserId;
	}
}
