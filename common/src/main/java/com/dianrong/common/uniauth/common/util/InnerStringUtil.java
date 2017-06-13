package com.dianrong.common.uniauth.common.util;

import java.util.regex.Pattern;

/**
 * 内部的字符串处理工具类.
 *
 * @author wanglin
 */
public class InnerStringUtil {

  /**
   * IPA账号的基本正则.
   */
  public static final Pattern IPA_ACCOUNT = Pattern.compile("^[a-z|A-Z]+[0-9]*$");

  /**
   * 判断账号是否为IPA账号.
   */
  public static boolean isIpaAccount(String account) {
    return IPA_ACCOUNT.matcher(account).matches();
  }
}
