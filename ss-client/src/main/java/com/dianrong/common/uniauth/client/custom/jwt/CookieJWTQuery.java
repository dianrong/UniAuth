package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.common.jwt.JWTConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;

/**
 * 获取Cookie中的JWT.
 * 
 * @author wanglin
 *
 */
@Order(-100)
public class CookieJWTQuery implements JWTQuery {

  /**
   * JWTCookie名称的后缀
   */
  private String jwtCookieNameSuffix;

  @Override
  public String getJWT(HttpServletRequest request) {
    String cookieName = JWTConstant.JWT_NAME;
    if (this.jwtCookieNameSuffix != null) {
      cookieName += this.jwtCookieNameSuffix;
    }
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : request.getCookies()) {
      if (cookieName.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }

  public String getJwtCookieNameSuffix() {
    return jwtCookieNameSuffix;
  }

  public void setJwtCookieNameSuffix(String jwtCookieNameSuffix) {
    this.jwtCookieNameSuffix = jwtCookieNameSuffix;
  }
}
