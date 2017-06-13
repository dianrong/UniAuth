package com.dianrong.common.uniauth.client.custom.model;

/**
 * 定义API返回的各种结果的code.
 *
 * @author wanglin
 */
public interface ResponseCode {

  /** 状态类. **/
  /**
   * 一个通用的结果状态码,成功.
   */
  int SUCCESS = 200;

  /**
   * 通用的状态结果码,失败.
   */
  int FAILURE = 500;
}
