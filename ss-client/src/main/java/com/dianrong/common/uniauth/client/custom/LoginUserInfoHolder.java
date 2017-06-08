package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.exp.UserNotLoginException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 提供便利的方式供业务系统获取当前登陆的用户.
 *
 * @author wanglin
 */
public final class LoginUserInfoHolder {

  /**
   * . 获取当前登陆用户的信息
   *
   * @return 当前登陆用户对象
   * @throws UserNotLoginException if not login
   */
  public static UserExtInfo getLoginUserInfo() throws UserNotLoginException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null) {
      throw new UserNotLoginException("need login");
    }
    Object principal = authentication.getPrincipal();
    if (principal == null || !(principal instanceof UserExtInfo)) {
      throw new UserNotLoginException("need login");
    }
    return (UserExtInfo) principal;
  }

  /**
   * . 获取当前登陆用户的租户id
   *
   * @return current login user tenancyId
   * @throws UserNotLoginException if not login
   */
  public static long getCurrentLoginUserTenancyId() throws UserNotLoginException {
    return getLoginUserInfo().getUserDto().getTenancyId();
  }
}
