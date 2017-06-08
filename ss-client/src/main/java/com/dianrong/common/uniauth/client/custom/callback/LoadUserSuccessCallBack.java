package com.dianrong.common.uniauth.client.custom.callback;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;

/**
 * 加载用户详情成功的回调函数.
 *
 * @author wanglin
 */
public interface LoadUserSuccessCallBack {

  /**
   * 登陆成功, load用户信息成功回调.
   *
   * @param userDetail 加载的用户详情(不会为空)
   */
  void loadUserSuccess(UserExtInfo userDetail);
}
