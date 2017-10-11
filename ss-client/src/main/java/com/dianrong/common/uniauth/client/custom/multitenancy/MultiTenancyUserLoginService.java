package com.dianrong.common.uniauth.client.custom.multitenancy;

import com.dianrong.common.uniauth.client.exp.UserLoginFailedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 提供多租户的情况下,用户登录的service.
 */
public interface MultiTenancyUserLoginService {

  UserDetails loadLoginUserDetails(String tenancyCode, String account, String password, String ip) throws UserLoginFailedException;
}
