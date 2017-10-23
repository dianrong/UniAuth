package com.dianrong.common.uniauth.common.client.enums;

import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Set;

/**
 * 定义Uniauth中的Authentication类型.
 *
 * @author wanglin
 */
public enum AuthenticationType {

  /**
   * 采用JWT来验证用户.
   */
  JWT(-200, "JWT"),

  /**
   * 采用CAS的方式来验证用户.
   */
  CAS(-100, "CAS"),

  /**
   * BasicAuth的验证方式.
   */
  BASIC_AUTH(-300, "BASIC_AUTH"),

  /**
   * 支持所有的验证的验证方式.
   */
  ALL(10000, AuthenticationType.class.getName() + "_ALL");

  /**
   * 支持所有的验证方式.
   */
  public static final String SUPPORTED_ALL_AUTHENTICATION_TYPE =
      AuthenticationType.class.getName() + "_ALL";

  private final Set<String> supportedTypes = Sets.newHashSet();

  private final int order;

  private AuthenticationType(int order, String... types) {
    supportedTypes.addAll(Arrays.asList(types));
    this.order = order;
  }

  public int getOrder() {
    return order;
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
