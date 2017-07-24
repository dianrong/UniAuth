package com.dianrong.common.uniauth.common.bean;

/**
 * 用户身份类型枚举.
 * 
 * @author wanglin
 */
public enum UserIdentityType {
  EMAIL("email"), PHONE("phone"), STAFF_NO("staff_no"), LDAP_DN("ldap_dn"), USER_GUID("user_guid");
  private final String type;

  private UserIdentityType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
