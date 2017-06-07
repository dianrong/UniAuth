package com.dianrong.common.uniauth.cas.exp;

import javax.security.auth.login.AccountException;

/**
 * . 用户密码不匹配异常
 *
 * @author wanglin
 */
public class UserPasswordNotMatchException extends AccountException {

  private static final long serialVersionUID = 1665206621245374336L;

  public UserPasswordNotMatchException() {
    super();
  }

  public UserPasswordNotMatchException(String msg) {
    super(msg);
  }
}
