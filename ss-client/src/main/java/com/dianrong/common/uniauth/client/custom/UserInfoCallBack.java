package com.dianrong.common.uniauth.client.custom;

/**
 * 用户登陆成功之后,扩充登陆成功的用户信息.
 * 
 * @author wanglin
 *
 */
public interface UserInfoCallBack {

  void fill(UserExtInfo userExtInfo);

}
