package com.dianrong.common.uniauth.common.switches;

import java.lang.reflect.Field;

/**
 * 配置接收器
 * @author dreamlee
 *
 */
public interface SwitchReceiver {
	
	
	public void receive(Field f,String config) throws Exception;

}
