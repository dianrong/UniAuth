package com.dianrong.common.uniauth.client.custom.basicauth;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;

public final class BasicAuthDetector {

  /**
   * Basic auth信息的header的名称.
   */
  public static final String HEADER_NAME = "Authorization";

  /**
   * Basic auth信息的开头值.
   */
  public static final String HEADER_VALUE_PREFIX = "Basic ";

  /**
   * 信息的分割符.
   */
  public static final String DELIMITER = ":";

  /**
   * 判断当前请求是否是BasicAuth请求.
   */
  public static boolean isBasicAuthRequest(HttpServletRequest request) {
    String header = request.getHeader(HEADER_NAME);
    if (header == null || !header.startsWith(HEADER_VALUE_PREFIX)) {
      return false;
    }
    return true;
  }

  /**
   * 获取请求中的BasicAuth信息.
   */
  public static BasicAuth getBasicAuthInfo(HttpServletRequest request)
      throws UnsupportedEncodingException {
    String header = request.getHeader(HEADER_NAME);
    if (header == null || !header.startsWith(HEADER_VALUE_PREFIX)) {
      return null;
    }
    byte[] base64Token = header.substring(HEADER_VALUE_PREFIX.length()).getBytes("UTF-8");
    byte[] decoded;
    try {
      decoded = Base64.decode(base64Token);
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException("Failed to decode basic authentication token");
    }
    String token = new String(decoded, "UTF-8");
    int delim = token.indexOf(DELIMITER);
    if (delim == -1) {
      throw new BadCredentialsException("Invalid basic authentication token");
    }
    String tenancyCode = token.substring(0, delim);

    token = token.substring(delim + 1);
    delim = token.indexOf(DELIMITER);
    if (delim == -1) {
      throw new BadCredentialsException("Invalid basic authentication token");
    }
    String account = token.substring(0, delim);
    String password = token.substring(delim + 1);
    return new BasicAuth(tenancyCode, account, password);
  }
}
