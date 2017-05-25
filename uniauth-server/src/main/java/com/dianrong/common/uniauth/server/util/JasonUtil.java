package com.dianrong.common.uniauth.server.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JasonUtil {

  private static ObjectMapper objectMapper =
      new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

  private JasonUtil() {

  }

  /**
   * 对转转化为Json.
   */
  public static String object2Jason(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * 将一种类型转换为新的类型.
   */
  public static <T> T o2o(Object fromValue, Class<T> toValueType) {
    return objectMapper.convertValue(fromValue, toValueType);
  }
}
