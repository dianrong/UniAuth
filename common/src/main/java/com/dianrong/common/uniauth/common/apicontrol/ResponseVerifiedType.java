package com.dianrong.common.uniauth.common.apicontrol;

import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * 访问接口的返回结果.
 *
 * @author wanglin
 */
public enum ResponseVerifiedType {

  LOGIN_SUCCESS, // 登陆成功

  TOKEN_AVAILABLE, // 传入的token可用

  AUTENTICATION_FAILED, // 账号密码错误

  TOKEN_INVALID, // TOKEN 验证失败

  TOKEN_EXPIRED, // token过期了

  INSUFFICIENT_PRIVILEGES, // 权限不够
  ;

  /**
   * Translate string to ResponseVerifiedType.
   *
   * @param typeStr String
   * @return null if typeStr is null or empty string
   * @throws IllegalArgumentException if typeStr has length, but it is invalid 
   */
  public static ResponseVerifiedType translateStrToResType(String typeStr) {
    if (StringUtil.strIsNullOrEmpty(typeStr)) {
      return null;
    }
    return ResponseVerifiedType.valueOf(typeStr);
  }
}
