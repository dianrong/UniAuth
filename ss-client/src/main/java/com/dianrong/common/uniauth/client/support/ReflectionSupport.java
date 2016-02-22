package com.dianrong.common.uniauth.client.support;

import java.lang.reflect.Field;

public class ReflectionSupport {

	private ReflectionSupport() {
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
}
