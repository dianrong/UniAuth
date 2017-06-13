package com.dianrong.common.uniauth.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpUtil {

  // add more 'password' related fields into PWD_FIELDS if necessary
  private static final String[] PWD_FIELDS = {"password", "originPassword", "passwordSalt"};
  private static Pattern[] pwdPatterns;

  private RegExpUtil() {
  }

  /**
   * 过滤掉信息中的敏感信息, 比如密码,密码盐.
   */
  public static String purgePassword(String jasonParam) {
    if (pwdPatterns == null) {
      pwdPatterns = new Pattern[PWD_FIELDS.length];
      for (int i = 0; i < PWD_FIELDS.length; i++) {
        pwdPatterns[i] = Pattern.compile("(.*?)\"" + PWD_FIELDS[i] + "\":\"(.*?)\"(.*)");
      }
    }
    if (jasonParam != null) {
      for (int i = 0; i < pwdPatterns.length; i++) {
        Matcher m = pwdPatterns[i].matcher(jasonParam);
        if (m.find()) {
          String password = m.group(2);

          if (password != null && !"".equals(password.trim())) {
            jasonParam = jasonParam.replace(password, "******");
          }
        }
      }
    }
    return jasonParam;
  }
}
