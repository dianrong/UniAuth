package com.dianrong.common.uniauth.common.apicontrol;

/**
 * 定义协议头
 * @author wanglin
 */
public interface HeaderKey {
    
    String REQUEST_TYPE = "api-control-request-type";
    
    String REQUEST_CONTENT = "api-control-request-content";
    
    String  REQUEST_ACCOUNT = "api-control-request-account";
    
    String  REQUEST_PASSWORD = "api-control-request-password";
    
    String RESPONSE_TYPE = "api-control-response-type";
    
    String RESPONSE_REULST = "api-control-response-result";
    
    String  RESPONSE_TOKEN = "api-control-response-token";
    
    String  RESPONSE_EXPIRETIME = "api-control-response-expireTime";
}
