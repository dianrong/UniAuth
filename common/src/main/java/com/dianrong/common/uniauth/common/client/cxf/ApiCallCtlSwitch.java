package com.dianrong.common.uniauth.common.client.cxf;

/**
 * API访问控制开关.
 *
 * @author wanglin
 */
public interface ApiCallCtlSwitch {

  /**
   * 判断API访问控制功能是否开启.
   *
   * @return true or false
   */
  boolean apiCtlOn();
}
