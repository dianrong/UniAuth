package com.dianrong.common.uniauth.client;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UniClientCommonService {
  /**
   * 获取当前的登陆用户信息.
   */
  public UserExtInfo getLoginUserInfo() {
    UserExtInfo userExtInfo = (UserExtInfo) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    return userExtInfo;
  }

}
