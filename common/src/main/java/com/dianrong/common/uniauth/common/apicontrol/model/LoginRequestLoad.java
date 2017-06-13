package com.dianrong.common.uniauth.common.apicontrol.model;

import com.dianrong.common.uniauth.common.util.Assert;
import java.io.Serializable;

/**
 * 登陆请求载体.
 *
 * @author wanglin
 */
public class LoginRequestLoad implements Serializable {

  private static final long serialVersionUID = -4290231289632307116L;

  private String account;

  private String password;

  /**
   * 设置登陆请求的账号,密码.
   */
  public LoginRequestLoad(String account, String password) {
    super();
    Assert.notNull(account);
    Assert.notNull(password);
    this.account = account;
    this.password = password;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }
}
