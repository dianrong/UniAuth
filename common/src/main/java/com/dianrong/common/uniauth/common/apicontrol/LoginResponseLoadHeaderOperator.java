package com.dianrong.common.uniauth.common.apicontrol;

import com.dianrong.common.uniauth.common.apicontrol.model.LoginResponseLoad;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 定义协议操作接口.
 *
 * @author wanglin
 */
public class LoginResponseLoadHeaderOperator implements PtHeaderOperator<LoginResponseLoad> {

  private StringHeaderValueOperator stringHeaderOperator;

  public LoginResponseLoadHeaderOperator(StringHeaderValueOperator stringHeaderOperator) {
    Assert.notNull(stringHeaderOperator);
    this.stringHeaderOperator = stringHeaderOperator;
  }

  @Override
  public LoginResponseLoad getHeader(String key) {
    return new LoginResponseLoad(stringHeaderOperator.getHeader(HeaderKey.RESPONSE_TOKEN),
        Long.valueOf(stringHeaderOperator.getHeader(HeaderKey.RESPONSE_EXPIRETIME)));
  }

  @Override
  public void setHeader(String key, LoginResponseLoad value) {
    Assert.notNull(value);
    stringHeaderOperator.setHeader(HeaderKey.RESPONSE_TOKEN, value.getToken());
    stringHeaderOperator
        .setHeader(HeaderKey.RESPONSE_EXPIRETIME, String.valueOf(value.getExpireTime()));
  }
}
