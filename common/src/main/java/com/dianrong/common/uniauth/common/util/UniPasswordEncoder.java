package com.dianrong.common.uniauth.common.util;

public class UniPasswordEncoder {

  private UniPasswordEncoder() {

  }

  /**
   * 加密密码.
   */
  public static String encodePassword(String rawPass, Object salt) {
    String passwordSalt = (salt == null ? null : salt.toString());
    byte[] salts = Base64.decode(passwordSalt);

    byte[] saltedPass = AuthUtils.digest(rawPass, salts);
    return Base64.encode(saltedPass);
  }

  public static boolean isPasswordValid(String encPass, String rawPass, Object salt) {
    return encPass.equals(encodePassword(rawPass, salt));
  }
}
