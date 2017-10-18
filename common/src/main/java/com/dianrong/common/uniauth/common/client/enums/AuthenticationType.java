package com.dianrong.common.uniauth.common.client.enums;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 定义Uniauth中的Authentication类型.
 * 
 * @author wanglin
 *
 */
public enum AuthenticationType {

  /**
   * 采用JWT来验证用户.
   */
  JWT("JWT"),

  /**
   * 采用CAS的方式来验证用户.
   */
  CAS("CAS"),

  /**
   * BasicAuth的验证方式.
   */
  BASIC_AUTH("BASIC_AUTH"),

  /**
   * 支持所有的验证的验证方式.
   */
  ALL(AuthenticationType.class.getName() + "_ALL");

  /**
   * 支持所有的验证方式.
   */
  public static final String SUPPORTED_ALL_AUTHENTICATION_TYPE =
      AuthenticationType.class.getName() + "_ALL";

  private Set<String> supportedTypes = Sets.newHashSet();

  private AuthenticationType(String... types) {
    supportedTypes.addAll(Arrays.asList(types));
  }

  /**
   * 判断传入的Type类型,当前是否支持.
   * 
   * @param type 指定的AuthenticationType.
   * @return 是否支持验证指定的类型.
   */
  public boolean isSupported(AuthenticationType type) {
    if (type == null) {
      return false;
    }
    if (this.supportedTypes.contains(SUPPORTED_ALL_AUTHENTICATION_TYPE)) {
      return true;
    }
    return this.supportedTypes.contains(type.toString());
  }
}
