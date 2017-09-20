package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneTranslator extends AbstractAttributeTypeTranslator {

  @Override
  public Object doToDatabaseType(String attribute) {
    Phone phone = new Phone(attribute);
    return phone.getPhone();
  }
}
