package com.dianrong.common.uniauth.cas.service.jwt;

import com.dianrong.common.uniauth.common.jwt.JWTConstant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;

/**
 * 处理JWT的cookie.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class JWTCookieGenerator extends CookieGenerator {

  public JWTCookieGenerator() {
    super();
    super.setCookieName(JWTConstant.JWT_COOKIE_NAME);
  }

  public JWTCookieGenerator(String cookieNameSuffix) {
    super();
    String cookieName = StringUtils.hasText(cookieNameSuffix)
        ? JWTConstant.JWT_COOKIE_NAME + cookieNameSuffix : JWTConstant.JWT_COOKIE_NAME;
    super.setCookieName(cookieName);
  }
  
  /**
   * 获取Cookie信息.
   */
  public String retrieveCookieValue(final HttpServletRequest request) {
      try {
          final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(
                  request, getCookieName());
          return cookie == null ? null : cookie.getValue();
      } catch (final Exception e) {
          log.debug(e.getMessage(), e);
      }
      return null;
  }
}
