package com.dianrong.uniauth.ssclient.config.support;

import java.lang.reflect.Field;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReflectionUtils {
  public static Object getField(Object targetObj, String fieldName){
    Object object = null;
    try{
        Class<?> targetClazz = targetObj.getClass();
        Field field = null;
        while (field == null && targetClazz != null) {
          field = targetClazz.getDeclaredField(fieldName);
          if (field == null ) {
            targetClazz = targetClazz.getSuperclass();
          }
        }
        field.setAccessible(true);
        object = field.get(targetObj);
    }catch(Exception e){
        log.warn("ReflectionUtils.getField exception", e);
    }
    return object;
}
}
