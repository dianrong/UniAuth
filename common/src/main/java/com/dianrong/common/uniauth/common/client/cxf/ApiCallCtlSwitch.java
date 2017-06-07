package com.dianrong.common.uniauth.common.client.cxf;

/**
 * api访问控制开关
 *
 * @author wanglin
 */
public interface ApiCallCtlSwitch {

  /**
   * 判断api访问控制功能是否开启
   *
   * @return true or false
   */
  boolean apiCtlOn();
}
