package com.dianrong.common.uniauth.common.apicontrol.client;

/**
 * 当前客户端的状态.
 *
 * @author wanglin
 */
public enum ClientStatus {

  ANONYMOUS, // 采用匿名的状态访问

  TOKEN, // 通过token进行访问

  NEED_LOGIN, // 需要进行登陆

  LOGGING, // 正在登陆
  ;
}
