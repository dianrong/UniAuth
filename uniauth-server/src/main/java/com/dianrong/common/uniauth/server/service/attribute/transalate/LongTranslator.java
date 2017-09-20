package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class LongTranslator extends AbstractAttributeTypeTranslator {

  @Override
  public Object doToDatabaseType(String attribute) {
    return Long.parseLong(attribute);
  }
}
