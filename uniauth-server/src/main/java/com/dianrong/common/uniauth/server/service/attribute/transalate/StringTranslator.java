package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class StringTranslator extends AbstractAttributeTypeTranslator {

  @Override public Object doToRealType(String attribute) {
    return attribute;
  }
}
