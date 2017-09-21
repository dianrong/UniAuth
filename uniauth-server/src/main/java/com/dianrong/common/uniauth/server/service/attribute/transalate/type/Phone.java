package com.dianrong.common.uniauth.server.service.attribute.transalate.type;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.service.attribute.exp.InvalidPropertyValueException;

import java.io.Serializable;

public class Phone implements Serializable{

  private String phone;

  public Phone(String phone) {
    if (!StringUtil.isPhoneNumber(phone)) {
      throw new InvalidPropertyValueException(phone + " is a invalid phone string.", "Phone",
          phone);
    }
    this.phone = phone;
  }

  public String getPhone() {
    return phone;
  }

  @Override public String toString() {
    return this.phone;
  }
}
