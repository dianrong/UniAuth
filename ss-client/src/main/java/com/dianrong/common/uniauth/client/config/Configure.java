package com.dianrong.common.uniauth.client.config;

/**
 * 手动构造bean对象
 * @author wanglin
 */
public interface Configure<T> {
	
	/**
	 * create a new T
	 * @return new T
	 */
	T create();
	
	/**
	 * judge whether the class type is supported
	 * @param cls the type
	 * @return true if the class is supported
	 */
	boolean isSupport(Class<?> cls);
}
