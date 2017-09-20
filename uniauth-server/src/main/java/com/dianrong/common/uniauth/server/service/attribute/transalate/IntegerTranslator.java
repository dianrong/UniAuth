package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class IntegerTranslator extends AbstractAttributeTypeTranslator {

  @Override
  public Object doToRealType(String attribute) {
    return Integer.parseInt(attribute);
  }
}
