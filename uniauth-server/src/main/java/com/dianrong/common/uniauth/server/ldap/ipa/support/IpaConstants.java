package com.dianrong.common.uniauth.server.ldap.ipa.support;

public interface IpaConstants {

  /**
   * 定义整个IPA操作的base.
   */
  String BASE = "cn=accounts,dc=ipa,dc=dianrong,dc=com";
  /**
   * 定义组信息操作的base.
   */
  String GROUP_BASE = "cn=groups";

  /**
   * 定义用户信息操作的base.
   */
  String USER_BASE = "cn=users";

  /**
   * 定义组操作的全路径.
   */
  String GROUP_URL = GROUP_BASE + "," + BASE;
}
