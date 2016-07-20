package com.dianrong.common.uniauth.common.util;

import com.dianrong.common.uniauth.common.exp.CfgMissingException;

public class CheckSDKCfg {
	private CheckSDKCfg(){
	}
	
	public static void checkSDKCfg(String bindValue) {
		if(bindValue == null || "".equals(bindValue.toString())){
			throw new CfgMissingException("Missing uniauth endpoint.");
		}
	}
}
