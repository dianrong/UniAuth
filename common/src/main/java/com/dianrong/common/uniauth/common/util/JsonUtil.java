package com.dianrong.common.uniauth.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

  private static ObjectMapper objectMapper = new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL);

  private JsonUtil() {
  }

  /**
   * 对象转JSON.
   */
  public static String object2Jason(Object obj) {
    try {
      return objectMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      return null;
    }
  }

  /**
   * JSON转化为对象.
   */
  public static <T> T jsonToObject(String json, Class<T> toValueType) {
    if (json == null) {
      return null;
    }
    try {
      return objectMapper.readValue(json, toValueType);
    } catch (Exception e) {
      throw new RuntimeException("json to object failed :" + e.getMessage());
    }
  }

  /**
   * 将一种类型转换为新的类型.
   */
  public static <T> T o2o(Object fromValue, Class<T> toValueType) {
    return objectMapper.convertValue(fromValue, toValueType);
  }
}
