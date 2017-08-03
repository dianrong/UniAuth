package com.dianrong.common.uniauth.common.client.enums;

/**
 * 定义Uniauth中的Authentication类型.
 * 
 * @author wanglin
 *
 */
public enum AuthenticationType {

  /**
   * 采用JWT来验证用户.
   */
  JWT, 
  
  /**
   * 采用CAS的方式来验证用户.
   */
  CAS
}
