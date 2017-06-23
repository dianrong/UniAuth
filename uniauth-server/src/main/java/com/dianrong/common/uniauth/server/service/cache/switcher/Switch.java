package com.dianrong.common.uniauth.server.service.cache.switcher;

/**
 * 一个简单的开关控制接口.
 */
public interface Switch {
  /**
   * 开关.
   * @return 开关是否打开.
   */
  boolean on();
}
