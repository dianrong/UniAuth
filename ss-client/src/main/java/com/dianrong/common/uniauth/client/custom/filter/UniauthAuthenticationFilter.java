package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

import javax.servlet.Filter;

public interface UniauthAuthenticationFilter extends Filter {

  /**
   * 获取当前的AuthenticationFilter的验证类型.
   * 
   * @return 返回不能为空,必须指定一种类型.
   */
  AuthenticationType authenticationType();

}
