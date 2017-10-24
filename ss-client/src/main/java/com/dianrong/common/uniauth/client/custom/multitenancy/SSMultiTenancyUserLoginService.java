package com.dianrong.common.uniauth.client.custom.multitenancy;

import com.dianrong.common.uniauth.client.exp.UserLoginFailedException;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import com.dianrong.common.uniauth.common.client.UniClientFacade;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

@Slf4j
public class SSMultiTenancyUserLoginService implements MultiTenancyUserLoginService {

  /**
   * 获取用户详情的service.
   */
  @Autowired
  private MultiTenancyUserDetailsService multiTenancyUserDetailsService;

  @Autowired
  private UniClientFacade uniClientFacade;


  @Override
  public UserDetails loadLoginUserDetails(String tenancyCode, String account, String password, String ip)
      throws UserLoginFailedException {
    Assert.notNull(tenancyCode, "Login need tenancyCode");
    Assert.notNull(account, "Login need account");
    Assert.notNull(password, "Login need password");
    Assert.notNull(ip, "Login need request ip");
    // 登录
    LoginParam loginParam = new LoginParam();
    loginParam.setTenancyCode(tenancyCode).setAccount(account).setPassword(password).setIp(ip);
    Response<UserDto> response = uniClientFacade.getUserResource().systemLogin(loginParam);
    if (!ObjectUtils.isEmpty(response.getInfo())) {
      String msg = String.format("Login failed. Account:%s, the reason:%s", account, response.getInfo().get(0).getMsg());
      log.error(msg);
      throw new UserLoginFailedException(msg);
    }
    UserDto userDto = response.getData();
    log.debug("Account:{}, tenancyId:{}, login success", account, userDto.getTenancyId());

    // 获取用户详细信息
    return multiTenancyUserDetailsService.loadUserByUsername(account, userDto.getTenancyId());
  }
}
