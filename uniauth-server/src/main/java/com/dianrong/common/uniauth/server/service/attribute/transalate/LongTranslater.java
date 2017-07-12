package com.dianrong.common.uniauth.server.service.attribute.transalate;

public class LongTranslater extends AbstractAttributeTypeTranslater {

  @Override
  public Object doToRealType(String attribute) {
    return Long.parseLong(attribute);
  }

  @Override
  public String doToString(Object obj) {
    return obj.toString();
  }
}
