package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.server.exp.AppException;

public class CheckEmpty {
	
	private CheckEmpty(){}
	
	public static void checkId(PrimaryKeyParam primaryKeyParam, String errorMsg){
		if(primaryKeyParam == null || primaryKeyParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", errorMsg));
		}
	}
}
