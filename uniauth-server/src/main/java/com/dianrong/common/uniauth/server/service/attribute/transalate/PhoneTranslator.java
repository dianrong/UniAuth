package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneTranslator extends AbstractAttributeTypeTranslator {


  @Override
  public Object doToRealType(String attribute) {
    return new Phone(attribute);
  }
}
