package com.dianrong.common.uniauth.server.service.multidata;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.server.exp.AppException;

import org.springframework.core.Ordered;

/**
 * 多数据源身份认证接口.
 */
public interface UserAuthentication extends Ordered {

  /**
   * 用户登陆.
   * 
   * @throws AppException 登陆失败
   */
  UserDto login(LoginParam loginParam);

  /**
   * 获取用户详细信息.
   */
  UserDetailDto getUserDetailInfo(LoginParam loginParam);

  /**
   * 根据用户的Email或者Phone获取用户信息. 定义该接口，主要是为了兼容以前的实现.
   */
  UserDto getUserByEmailOrPhone(LoginParam loginParam);

  /**
   * 指定当前实现是否支持指定的数据实现.
   */
  boolean supported(LoginParam loginParam);
}
