package com.dianrong.common.uniauth.common.apicontrol;

import com.dianrong.common.uniauth.common.apicontrol.model.LoginRequestLoad;
import com.dianrong.common.uniauth.common.util.Assert;

/**
 * 定义协议操作接口.
 *
 * @author wanglin
 */
public class LoginRequestLoadHeaderOperator implements PtHeaderOperator<LoginRequestLoad> {

  private StringHeaderValueOperator stringHeaderOperator;

  public LoginRequestLoadHeaderOperator(StringHeaderValueOperator stringHeaderOperator) {
    Assert.notNull(stringHeaderOperator);
    this.stringHeaderOperator = stringHeaderOperator;
  }

  @Override
  public LoginRequestLoad getHeader(String key) {
    return new LoginRequestLoad(stringHeaderOperator.getHeader(HeaderKey.REQUEST_ACCOUNT),
        stringHeaderOperator.getHeader(HeaderKey.REQUEST_PSWD));
  }

  @Override
  public void setHeader(String key, LoginRequestLoad value) {
    Assert.notNull(value);
    stringHeaderOperator.setHeader(HeaderKey.REQUEST_ACCOUNT, value.getAccount());
    stringHeaderOperator.setHeader(HeaderKey.REQUEST_PSWD, value.getPassword());
  }
}
