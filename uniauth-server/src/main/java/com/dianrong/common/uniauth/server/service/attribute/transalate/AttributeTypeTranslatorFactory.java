package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Email;
import com.dianrong.common.uniauth.server.service.attribute.transalate.type.Phone;
import com.google.common.collect.Maps;

import java.sql.Date;
import java.util.Map;

/**
 * 获取各种类型的AttributeTypeTranslator.
 */
public final class AttributeTypeTranslatorFactory {

  private static final Map<Class<?>, AttributeTypeTranslator> ATTRIBUTE_TYPE_TRANSLATERS =
      Maps.newConcurrentMap();

  static {
    ATTRIBUTE_TYPE_TRANSLATERS.put(Date.class, new DateTranslator());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Integer.class, new IntegerTranslator());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Long.class, new LongTranslator());
    ATTRIBUTE_TYPE_TRANSLATERS.put(String.class, new StringTranslator());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Email.class, new EmailTranslator());
    ATTRIBUTE_TYPE_TRANSLATERS.put(Phone.class, new PhoneTranslator());
  }

  /**
   * 根据不同的类型获取AttributeTypeTranslator.
   */
  public static final AttributeTypeTranslator getTranslator(Class<?> clz) {
    Assert.notNull(clz);
    AttributeTypeTranslator translator = ATTRIBUTE_TYPE_TRANSLATERS.get(clz);
    if (translator == null) {
      throw new UniauthCommonException(clz.getName() + " is not supported!");
    }
    return translator;
  }
}
