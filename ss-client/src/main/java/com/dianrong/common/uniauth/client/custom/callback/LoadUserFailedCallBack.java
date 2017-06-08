package com.dianrong.common.uniauth.client.custom.callback;

/**
 * Load用户详情失败调用接口.
 *
 * @author wanglin
 */
public interface LoadUserFailedCallBack {

  /**
   * 登陆成功,但是load用户信息失败.
   *
   * @param identity 用户identity
   * @param tenancyId 租户id
   * @param cause 错误异常
   */
  void loadUserFailed(String identity, Long tenancyId, Throwable cause);

}
