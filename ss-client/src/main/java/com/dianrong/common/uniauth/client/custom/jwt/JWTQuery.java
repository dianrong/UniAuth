package com.dianrong.common.uniauth.client.custom.jwt;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于从请求中获取JWT.
 * 
 * @author wanglin
 *
 */
public interface JWTQuery {

  /**
   * 从请求中获取JWT信息,如果没有则返回null.
   */
  String getJWT(HttpServletRequest request);
}
