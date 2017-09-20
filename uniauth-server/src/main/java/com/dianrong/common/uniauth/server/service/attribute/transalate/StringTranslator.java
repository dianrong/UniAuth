package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class StringTranslator extends AbstractAttributeTypeTranslator {

  @Override
  public Object doToDatabaseType(String attribute) {
    return attribute;
  }
}
