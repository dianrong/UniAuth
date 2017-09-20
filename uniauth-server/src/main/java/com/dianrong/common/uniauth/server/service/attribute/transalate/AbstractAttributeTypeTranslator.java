package com.dianrong.common.uniauth.server.service.attribute.transalate;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractAttributeTypeTranslator implements AttributeTypeTranslator {
  
  @Override
  public Object toDatabaseType(String attribute) {
    if (StringUtils.isBlank(attribute)) {
      return null;
    }
    return doToDatabaseType(attribute);
  }
  
  @Override
  public String toString(Object obj) {
    if (obj == null) {
      return null;
    }
    return doToString(obj);
  }
  
  /**
   * 从字符串转化成一个数据能处理的对象.
   */
  public abstract Object doToDatabaseType(String attribute);
  
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
