package com.dianrong.common.uniauth.common.customer.basicauth.util;

/**
 * Created by denghb on 6/20/17.
 */
public class AuthorityStringUtil {

  /**
   * 转换Role为Spring security认识的类型.
   */
  public static String roleAuthrorityFormat(String permType) {
    if (permType == null) {
      return permType;
    }
    String defaultRolePrefix = "ROLE_";
    if (permType.startsWith(defaultRolePrefix)) {
      return permType;
    }
    return defaultRolePrefix + permType;
  }
}
