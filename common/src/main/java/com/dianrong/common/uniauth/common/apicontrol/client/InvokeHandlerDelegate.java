package com.dianrong.common.uniauth.common.apicontrol.client;

import java.lang.reflect.Method;

/**
 * delegate for invokeHandler
 * 
 * @author wanglin
 */
public interface InvokeHandlerDelegate {
    /**
     * delegate for invoke(Object proxy, Method method, Object[] args)
     * 
     * @param target target
     * @param proxy proxy
     * @param method method
     * @param args args
     * @return invoke result
     * @throws Throwable
     */
    Object invoke(Object target, Object proxy, Method method, Object[] args) throws Throwable;
}
