package com.dianrong.common.uniauth.client.custom.basicauth;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;

public class BasicAuth {
  private final String tenancyCode;
  private final String account;
  private final String password;

  public BasicAuth(String tenancyCode, String account, String password) {
    Assert.notNull(tenancyCode, "TenancyCode can not be null");
    Assert.notNull(account, "Account can not be null");
    this.tenancyCode = tenancyCode;
    this.account = account;
    this.password = password;
  }

  public String getMd5() {
    StringBuilder sb = new StringBuilder();
    sb.append(tenancyCode).append(":").append(account).append(":").append(password);
    return StringUtil.md5(sb.toString());
  }

  public String getTenancyCode() {
    return tenancyCode;
  }

  public String getAccount() {
    return account;
  }

  public String getPassword() {
    return password;
  }
}
