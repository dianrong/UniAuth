package com.dianrong.common.uniauth.cas.util;

import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class Test {

    
    public static void main(String args[]) {
        UniClientFacade uniClientFacade = new UniClientFacade("http://uniauth-dev.sl.com/ws/rs"); 
        LoginParam loginParam = new LoginParam();
        loginParam.setAccount("xxxxxx账号");
        loginParam.setPassword("password");
        uniClientFacade.getUserResource().login(loginParam);
    }
}
