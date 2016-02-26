package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.exp.AppException;

/**
 * Created by Arc on 27/1/16.
 */
public class ParamCheck {
    private ParamCheck(){}
    public static void checkStatus(Byte b){
        if(b == null){
            throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.empty", "status"));
        } else if(!AppConstants.ONE_Byte.equals(b) && !AppConstants.ZERO_Byte.equals(b)) {
            throw new AppException(InfoName.BAD_REQUEST, UniBundle.getMsg("common.parameter.status.invalid"));
        }
    }
}
