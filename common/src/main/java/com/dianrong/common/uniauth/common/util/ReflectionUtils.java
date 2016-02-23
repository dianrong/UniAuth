package com.dianrong.common.uniauth.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

	private ReflectionUtils() {
	}

	public static Object getField(Object targetObj, String fieldName, boolean isParentField){
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
			return field.get(targetObj);
		}catch(Exception e){
			return null;
		}
	}
	
	public static Object invokeStaticMethodWithoutParam(Class<?> clazz, String methodName){
		Object object = null;
		try{
			Method method = clazz.getMethod(methodName, new Class[0]);
			object = method.invoke(null, new Object[0]);
		}catch(Exception e){
		}
		return object;
	}
	
	public static Object invokeMethodWithoutParam(Object targetObj, String methodName){
		Object object = null;
		try{
			Method method = targetObj.getClass().getMethod(methodName, new Class[0]);
			object = method.invoke(targetObj, new Object[0]);
		}catch(Exception e){
		}
		return object;
	}
}
