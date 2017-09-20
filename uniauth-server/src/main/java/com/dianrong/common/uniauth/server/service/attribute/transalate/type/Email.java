package com.dianrong.common.uniauth.server.service.attribute.transalate.type;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;

public class Email {

  private String email;

  public Email(String email) {
    if (!StringUtil.isEmailAddress(email)) {
      throw new InvalidParameterTypeException(email + " is a invalid email string.");
    }
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  @Override public String toString() {
    return this.email;
  }
}
