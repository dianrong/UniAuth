package com.dianrong.common.uniauth.server.service.attribute.transalate;

/**
 * 用于扩展属性值的类型转换处理.
 * 需要兼容对null的处理.
 */
public interface AttributeTypeTranslater {
  
  /**
   * 从字符串转化为对应的类型.
   */
  Object toRealType(String attribute);
  
  /**
   * 从实际类型转换为一个字符.
   */
  String toString(Object obj);
}
