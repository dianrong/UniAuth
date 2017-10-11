package com.dianrong.common.uniauth.client.exp;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户登录失败异常.
 */
public class UserLoginFailedException extends AuthenticationException {

  private String account;

  public UserLoginFailedException(String msg) {
    super(msg);
  }

  public UserLoginFailedException(String msg, Throwable t) {
    super(msg, t);
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }
}
