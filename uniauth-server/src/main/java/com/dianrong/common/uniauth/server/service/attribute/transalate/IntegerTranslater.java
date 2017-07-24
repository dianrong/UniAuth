package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class IntegerTranslater extends AbstractAttributeTypeTranslater {

  @Override
  public Object doToRealType(String attribute) {
    return Integer.parseInt(attribute);
  }

  @Override
  public String doToString(Object obj) {
    return obj.toString();
  }
}
