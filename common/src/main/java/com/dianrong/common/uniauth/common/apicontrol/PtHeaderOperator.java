package com.dianrong.common.uniauth.common.apicontrol;

/**
 * 定义协议操作接口
 *
 * @author wanglin
 */
public interface PtHeaderOperator<T> {

  /**
   * get value from header
   *
   * @param key key
   * @return value
   */
  T getHeader(String key);

  /**
   * set header: key - > value
   *
   * @param key key
   * @param value value
   */
  void setHeader(String key, T value);
}
