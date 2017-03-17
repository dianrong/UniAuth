package com.dianrong.common.uniauth.server.util;

import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.server.exp.AppException;

public class CheckEmpty {
	
	private CheckEmpty(){}
	
	public static void checkParamId(PrimaryKeyParam primaryKeyParam, String errorMsg){
		if(primaryKeyParam == null || primaryKeyParam.getId() == null){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", errorMsg));
		}
	}
	
	public static void checkEmpty(Object idOrCollOrAnyObj, String errorMsg){
		if(idOrCollOrAnyObj == null || "".equals(idOrCollOrAnyObj.toString().trim())
				|| ((idOrCollOrAnyObj instanceof Collection) && (((Collection<?>)idOrCollOrAnyObj)).isEmpty())){
			throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", errorMsg));
		}
	}
	/**
	 * check all objs is blank("" or " "),then throw exception 
	 * @param errorMsg tip
	 * @param objs obj to check is blank
	 */
    public static void checkAllBlank(String errorMsg, String... objs){
	    if(objs == null || objs.length==0 ){
	        throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", errorMsg));	        
	    }
	    for (String id : objs) {
	        if(StringUtils.isNotBlank(id)){
	            return ;
	        }
        }
	    throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", errorMsg));	                    
	}
}
