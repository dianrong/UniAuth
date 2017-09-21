package com.dianrong.common.uniauth.server.service.attribute.transalate;

import com.dianrong.common.uniauth.server.service.attribute.exp.InvalidPropertyValueException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongTranslator extends AbstractAttributeTypeTranslator {

  @Override public Object doToRealType(String attribute) {
    try {
      return Long.parseLong(attribute);
    } catch (NumberFormatException e) {
      log.debug(attribute + " is a invalid number string.");
      throw new InvalidPropertyValueException(attribute + " is a invalid number string.", "Long",
          attribute);
    }
  }
}
