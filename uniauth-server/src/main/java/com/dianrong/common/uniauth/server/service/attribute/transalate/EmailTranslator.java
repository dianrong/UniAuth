package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Email;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailTranslator extends AbstractAttributeTypeTranslator {

  @Override
  public Object doToDatabaseType(String attribute) {
    Email email = new Email(attribute);
    return email.getEmail();
  }
}
