package com.dianrong.common.uniauth.common.switches;

import java.lang.reflect.Field;
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
		Object cvalue = TypeConverter.convert(f.getType(), config);
		if(cvalue != null){
			if(f.getType() == AtomicBoolean.class){
				AtomicBoolean value = (AtomicBoolean)f.get(null);
				value.set((Boolean)cvalue);
			}else if(f.getType() == AtomicInteger.class){
				AtomicInteger value = (AtomicInteger)f.get(null);
				value.set((Integer)cvalue);
			}else if(f.getType() == AtomicLong.class){
				AtomicLong value = (AtomicLong)f.get(null);
				value.set((Long)cvalue);
			}else{
				f.set(null, cvalue);
			}
			
		}
	}

}
