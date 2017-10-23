package com.dianrong.common.uniauth.client.custom.auth;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTypeDetector {

  /**
   * 通过请求判断得到当前的请求类型.
   * @param request Http请求,不为空.
   * @param args 附加参数列表.
   */
  AuthenticationType detect(HttpServletRequest request, Object... args);
}
