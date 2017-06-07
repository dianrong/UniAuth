package com.dianrong.common.uniauth.cas.util;

import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;

/**
 * . 国际化处理的bundle util
 *
 * @author wanglin
 */
public final class UniBundleUtil {

  /**
   * . 无参数的获取i8n value
   */
  public static String getMsg(MessageSource messageSource, String key) {
    Assert.notNull(messageSource, "get i18n msg, messageSource can not be null");
    return messageSource.getMessage(key, null, UniauthLocaleInfoHolder.getLocale());
  }


  /**
   * . 有参数的获取i8n value
   */
  public static String getMsg(MessageSource messageSource, String key, Object... arguments) {
    Assert.notNull(messageSource, "get i18n msg, messageSource can not be null");
    return messageSource.getMessage(key, arguments, UniauthLocaleInfoHolder.getLocale());
  }

  /**
   * . 判断传入的语言是当前选中的语言否
   */
  public static boolean isSelected(String localeStr) {
    if (localeStr == null) {
      return false;
    }
    return UniauthLocaleInfoHolder.getLocale().toString().equalsIgnoreCase(localeStr);
  }
}
