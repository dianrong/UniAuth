package com.dianrong.common.uniauth.common.bean;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StringUtils;

/**
 * 第三方账号的类型.
 */
@Slf4j
public enum ThirdAccountType {
  IPA, OA;

  public static ThirdAccountType getType(String type) {
    if (!StringUtils.hasText(type)) {
      return null;
    }
    try {
      return ThirdAccountType.valueOf(type);
    } catch (IllegalArgumentException ex) {
      log.warn("{} is not a valid ThirdAccountType string.", type);
    }
    return null;
  }
}
