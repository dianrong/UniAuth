package com.dianrong.common.uniauth.client.custom;

import javax.servlet.http.HttpServletRequest;

public interface CustomizedRedirectFormat {

  /**
   * 生成一个返回的对象.
   *
   * @param request HttpServletRequest
   * @param loginUrl 登陆的url
   * @return 返回结果需要反序列化的属性需要实现get方法
   */
  Object getRedirectInfo(HttpServletRequest request, String loginUrl);
}
