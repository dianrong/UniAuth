package com.dianrong.common.uniauth.common.jwt;

/**
 * 定义JWT会用到的常量.
 * 
 * @author wanglin
 *
 */
public interface JWTConstant {

  
  /**
   * Uniauth的JWT的名字.
   */
  String JWT_NAME = "uniauth_jwt";
  
  /**
   * 定义JWT中的一些常量信息.
   */
  String ISSUER = "uniauth-cas";
  String AUDIENCE = "all-uniauth-integrate-domains";
  String SUBJECT = "uniauth-integrate-system-login-token";
  
  // 用户的账号信息
  String IDENTITY_KEY = "user_identity";
  // 用户的租户id
  String TENANCY_ID_KEY = "user_tenancy_id";
}
