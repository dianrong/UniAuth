package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.google.common.collect.Maps;

import java.sql.Date;
import java.util.Map;

/**
 * 获取各种类型的AttributeTypeTranslator.
 */
public final class AttributeTypeTranslaterFactory {

  private static final Map<Class<?>, AttributeTypeTranslater> ATTRIBUTE_TYPE_TRANSLATERS =
      Maps.newConcurrentMap();

  static {
    ATTRIBUTE_TYPE_TRANSLATERS.put(Date.class, new DateTranslater());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Integer.class, new IntegerTranslater());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Long.class, new LongTranslater());
    ATTRIBUTE_TYPE_TRANSLATERS.put(String.class, new StringTranslater());
  }

  /**
   * 根据不同的类型获取AttributeTypeTranslater.
   */
  public static final AttributeTypeTranslater getTranslator(Class<?> clz) {
    Assert.notNull(clz);
    AttributeTypeTranslater translator = ATTRIBUTE_TYPE_TRANSLATERS.get(clz);
    if (translator == null) {
      throw new UniauthCommonException(clz.getName() + " is not supported!");
    }
    return translator;
  }
}
