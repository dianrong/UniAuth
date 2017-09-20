package com.dianrong.common.uniauth.server.service.attribute.transalate.type;

import com.dianrong.common.uniauth.common.util.StringUtil;
import com.dianrong.common.uniauth.server.service.attributerecord.exp.InvalidParameterTypeException;

public class Phone {

  private String phone;

  public Phone(String phone) {
    if (!StringUtil.isPhoneNumber(phone)) {
      throw new InvalidParameterTypeException(phone + " is a invalid phone string.");
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
