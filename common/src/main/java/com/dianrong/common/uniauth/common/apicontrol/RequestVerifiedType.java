package com.dianrong.common.uniauth.common.apicontrol;

import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * 指定请求api的控制类型.
 *
 * @author wanglin
 */
public enum RequestVerifiedType {

  LOGIN, // 提供账号密码进行登陆

  TOKEN, // 提供token进行身份验证

  ANONYMOUS // 匿名访问
  ;

  /**
   * Translate string to RequestVerifiedType.
   *
   * @param typeStr String
   * @return null if typeStr is null or empty string
   * @throws IllegalArgumentException if typeStr has length, but it's not a valid
   */
  public static RequestVerifiedType translateStrToReqType(String typeStr) {
    if (StringUtil.strIsNullOrEmpty(typeStr)) {
      return null;
    }
    return RequestVerifiedType.valueOf(typeStr);
  }
}
