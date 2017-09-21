package com.dianrong.common.uniauth.server.service.attribute.transalate.type;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.service.attribute.exp.InvalidPropertyValueException;

import java.io.Serializable;

public class Email implements Serializable {

  private String email;

  public Email(String email) {
    if (!StringUtil.isEmailAddress(email)) {
      throw new InvalidPropertyValueException(email + " is a invalid email string.", "Email",
          email);
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
