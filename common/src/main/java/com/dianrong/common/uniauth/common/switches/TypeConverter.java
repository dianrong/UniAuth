package com.dianrong.common.uniauth.common.switches;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Maps;

/**
 * 
 * @author dreamlee
 *
 */
public abstract class TypeConverter {
	
	private static Map<Class<?>,TypeConverter> CONVERTERMAPPINGS = Maps.newHashMap();
	
	static{
		TypeConverter booleanConverter = new TypeConverter() {
			@Override
			public Object convert(String config) {
				return Boolean.valueOf(config);
			}
		};
		TypeConverter integerConverter = new TypeConverter() {
			@Override
			public Object convert(String config) {
				return Integer.valueOf(config);
			}
		};
		
		TypeConverter floatConverter = new TypeConverter() {
			
			@Override
			public Object convert(String config) {
				return Float.valueOf(config);
			}
		};
		TypeConverter doubleConverter = new TypeConverter() {
			
			@Override
			public Object convert(String config) {
				return Double.valueOf(config);
			}
		};
		
		TypeConverter longConverter = new TypeConverter() {
			
			@Override
			public Object convert(String config) {
				return Long.valueOf(config);
			}
		};
		
		TypeConverter stringConverter = new TypeConverter() {
			
			@Override
			public Object convert(String config) {
				return config;
			}
		};
		CONVERTERMAPPINGS.put(Boolean.class, booleanConverter);
		CONVERTERMAPPINGS.put(boolean.class, booleanConverter);
		CONVERTERMAPPINGS.put(AtomicBoolean.class, booleanConverter);
		CONVERTERMAPPINGS.put(Integer.class, integerConverter);
		CONVERTERMAPPINGS.put(int.class, integerConverter);
		CONVERTERMAPPINGS.put(AtomicInteger.class, booleanConverter);
		CONVERTERMAPPINGS.put(Float.class, floatConverter);
		CONVERTERMAPPINGS.put(float.class, floatConverter);
		CONVERTERMAPPINGS.put(Double.class, doubleConverter);
		CONVERTERMAPPINGS.put(double.class, doubleConverter);
		CONVERTERMAPPINGS.put(Long.class, longConverter);
		CONVERTERMAPPINGS.put(long.class, longConverter);
		CONVERTERMAPPINGS.put(AtomicLong.class, booleanConverter);
		CONVERTERMAPPINGS.put(String.class, stringConverter);
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T convert(Class<T> clazz,String config){
		if(CONVERTERMAPPINGS.containsKey(clazz)){
			return (T)CONVERTERMAPPINGS.get(clazz).convert(config);
		}
		return null;
	}
	
	
	public abstract Object convert(String config);

}
