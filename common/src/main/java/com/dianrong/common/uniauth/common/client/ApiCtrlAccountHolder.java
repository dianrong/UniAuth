package com.dianrong.common.uniauth.common.client;

/**
 * 用于注入API权限访问的账号的接口.
 *
 * @author wanglin
 */
public interface ApiCtrlAccountHolder {

  /**
   * 返回账号信息.
   *
   * @return 账号信息
   */
  String getAccount();

  /**
   * 返回密码.
   *
   * @return 密码
   */
  String getPassword();
}
