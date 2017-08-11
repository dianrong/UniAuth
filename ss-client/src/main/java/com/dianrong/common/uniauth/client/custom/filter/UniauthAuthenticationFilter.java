package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;

public interface UniauthAuthenticationFilter extends Filter, Ordered{

  /**
   * 获取当前的AuthenticationFilter的验证类型.
   * 
   * @return 返回不能为空,必须指定一种类型.
   */
  AuthenticationType authenticationType();
  
  /**
   * 判断当前请求是否需要进行身份认证.
   * @return True or False
   */
  boolean requiresAuthentication(HttpServletRequest request,
      HttpServletResponse response);

}
