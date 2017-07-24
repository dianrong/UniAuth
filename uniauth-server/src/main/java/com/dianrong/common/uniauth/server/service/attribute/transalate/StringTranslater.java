package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class StringTranslater extends AbstractAttributeTypeTranslater {

  @Override
  public Object doToRealType(String attribute) {
    return attribute;
  }

  @Override
  public String doToString(Object obj) {
    return obj.toString();
  }
}
