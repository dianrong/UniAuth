package com.dianrong.common.uniauth.server.service.facade;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.util.InnerStringUtil;
import com.dianrong.common.uniauth.server.service.IpaService;
import com.dianrong.common.uniauth.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用于处理用户登陆相关API,兼容IPA和MySQL.
 *
 * @author wanglin
 */

@Service
public class LoginFacade {

  @Autowired
  private UserService userService;

  @Autowired
  private IpaService ipaService;

  /**
   * 用户登陆.
   */
  public UserDto login(LoginParam loginParam) {
    if (InnerStringUtil.isIpaAccount(loginParam.getAccount())) {
      return ipaService.login(loginParam);
    }
    return userService.login(loginParam);
  }

  /**
   * 获取用户详细信息.
   */
  public UserDetailDto getUserDetailInfo(LoginParam loginParam) {
    if (InnerStringUtil.isIpaAccount(loginParam.getAccount())) {
      return ipaService.getUserDetailInfo(loginParam);
    }
    return userService.getUserDetailInfo(loginParam);
  }

  /**
   * 根据用户的邮箱或者电话获取用户信息.
   */
  public UserDto getUserByEmailOrPhone(LoginParam loginParam) {
    if (InnerStringUtil.isIpaAccount(loginParam.getAccount())) {
      return ipaService.getUserByEmailOrPhone(loginParam);
    }
    return userService.getUserByEmailOrPhone(loginParam);
  }
}
