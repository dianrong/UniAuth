package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.exp.ZkConfigMissingException;

public class CheckZkConfig {
	private CheckZkConfig(){
	}
	
	public static void checkZkConfig(String bindValue, String key, String configPath) {
		if(bindValue == null || "".equals(bindValue.toString())){
			throw new ZkConfigMissingException("Zookeeper required parameter for " + key + " missing [config path = " + configPath + "].");
		}
	}
}
