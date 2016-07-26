package com.dianrong.common.uniauth.common.switches;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 默认的接收器，此接收器只处理基本类型
 * @author dreamlee
 *
 */
public class DefaultSwitchReceiver implements SwitchReceiver {

	@Override
	public void receive(Field f,String config) throws Exception {
		if(f.getType() == String.class){
			f.set(null, config);
		}else if(f.getType() == Integer.class || f.getType() == int.class){
			f.set(null, Integer.valueOf(config));
		}else if(f.getType() == Boolean.class || f.getType() == boolean.class){
			f.set(null, Boolean.valueOf(config));
		}else if(f.getType() == Float.class || f.getType() == float.class){
			f.set(null, Float.valueOf(config));
		}else if(f.getType() == Long.class || f.getType() == long.class){
			f.set(null, Long.valueOf(config));
		}else if(f.getType() == AtomicBoolean.class){
			AtomicBoolean value = (AtomicBoolean)f.get(null);
			value.set(Boolean.valueOf(config));
		}else if(f.getType() == AtomicInteger.class){
			AtomicInteger value = (AtomicInteger)f.get(null);
			value.set(Integer.valueOf(config));
		}else if(f.getType() == AtomicLong.class){
			AtomicLong value = (AtomicLong)f.get(null);
			value.set(Long.valueOf(config));
		}
	}

}
