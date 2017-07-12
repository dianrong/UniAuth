package com.dianrong.common.uniauth.server.service.attribute.transalate;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAttributeTypeTranslater implements AttributeTypeTranslater {
  
  @Override
  public Object toRealType(String attribute) {
    if (StringUtils.isBlank(attribute)) {
      return null;
    }
    return doToRealType(attribute);
  }
  
  @Override
  public String toString(Object obj) {
    if (obj == null) {
      return null;
    }
    return doToString(obj);
  }
  
  /**
   * 从字符串转化为对应的类型.
   */
  public abstract Object doToRealType(String attribute);
  
  /**
   * 从实际类型转换为一个字符.
   */
  public abstract String doToString(Object obj);
}
