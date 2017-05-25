package com.dianrong.common.uniauth.server.data.entity.ext;

import com.dianrong.common.uniauth.server.data.entity.UserExtendVal;

public class UserExtendValExt extends UserExtendVal {

  private String extendCode;
  private String extendDescription;

  public String getExtendCode() {
    return extendCode;
  }

  public void setExtendCode(String extendCode) {
    this.extendCode = extendCode;
  }

  public String getExtendDescription() {
    return extendDescription;
  }

  public void setExtendDescription(String extendDescription) {
    this.extendDescription = extendDescription;
  }
}
