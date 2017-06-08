package com.dianrong.common.uniauth.common.apicontrol.model;

import java.io.Serializable;

/**
 * 登陆返回结果载体.
 *
 * @author wanglin
 */
public class LoginResponseLoad implements Serializable {

  private static final long serialVersionUID = -3385818323486452185L;

  private final String token;

  private final long expireTime;

  /**
   * 返回登陆请求的结果, 包含JWT和过期时间.
   */
  public LoginResponseLoad(String token, long expireTime) {
    super();
    this.token = token;
    this.expireTime = expireTime;
  }

  public String getToken() {
    return token;
  }

  public long getExpireTime() {
    return expireTime;
  }
}
