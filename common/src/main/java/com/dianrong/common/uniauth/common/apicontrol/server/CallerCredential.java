package com.dianrong.common.uniauth.common.apicontrol.server;

import java.io.Serializable;

/**
 * 调用者的信息
 * @author wanglin
 */
public interface CallerCredential<T extends Serializable> extends Serializable, WillExpired {
    /**
     * return caller name
     * @return
     */
    String getCallerName();
    
    /**
     * get account 
     * @return
     */
    String getAccount();
    
    /**
     * get permission information
     * @return
     */
    T getPermissionInfo();
}
