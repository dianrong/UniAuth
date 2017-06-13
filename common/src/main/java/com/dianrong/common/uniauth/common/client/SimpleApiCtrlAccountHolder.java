package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.util.Assert;
import lombok.extern.slf4j.Slf4j;

/**
 * 存放uniauth-server Api访问的权限认证的账号信息.
 *
 * @author wanglin
 */
@Slf4j
public class SimpleApiCtrlAccountHolder implements ApiCtrlAccountHolder {

  private String account;

  private String password;

  @Override
  public String getAccount() {
    return this.account;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  /**
   * 设置账号.
   */
  public void setAccount(String account) {
    Assert.notNull(account);
    this.account = account;
  }

  /**
   * 设置密码.
   */
  public void setPassword(String password) {
    if (password == null) {
      log.warn("api access account's password is null");
    }
    this.password = password == null ? "" : password;
  }
}
