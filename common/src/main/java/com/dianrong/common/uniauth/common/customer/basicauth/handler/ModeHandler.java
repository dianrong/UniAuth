package com.dianrong.common.uniauth.common.customer.basicauth.handler;

import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.customer.basicauth.mode.PermissionType;

import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Created by denghb on 6/21/17.
 */
public interface ModeHandler {

  /**
   * 获取权限的处理器.
   * @param domainCode 域名code.
   * @param permissionType 域允许的权限类型.
   */
  ArrayList<SimpleGrantedAuthority> handle(UserDetailDto userDetailDto, String domainCode,
      PermissionType permissionType);
}
