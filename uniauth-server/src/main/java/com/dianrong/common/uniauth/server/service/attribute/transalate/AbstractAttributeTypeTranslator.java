package com.dianrong.common.uniauth.server.service.attribute.transalate;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAttributeTypeTranslator implements AttributeTypeTranslator {
  
  @Override public Object toDatabaseType(String attribute) {
    return toRealType(attribute);
  }

  @Override public Object toRealType(String attribute) {
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

  public abstract Object doToRealType(String attribute);
  
  /**
   * 从实际类型转换为一个字符.
   */
  public String doToString(Object obj) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }
}
