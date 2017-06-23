package com.dianrong.common.uniauth.common.server;

import java.util.Locale;

/**
 * . 用户存放request请求的local信息,uniauth 国际化的专用辅助类
 *
 * @author wanglin
 */
public final class UniauthLocaleInfoHolder {

  /**
   * . 只存放locale信息
   */
  private static final ThreadLocal<Locale> localeInfo = new ThreadLocal<Locale>() {
    @Override
    protected Locale initialValue() {
      return Locale.getDefault();
    }
  };

  /**
   * Set new locale info into holder.
   */
  public static void setLocale(Locale newLocale) {
    if (newLocale == null) {
      newLocale = Locale.getDefault();
    }
    localeInfo.set(newLocale);
  }

  /**
   * . get locale info from holder
   *
   * @return Locale not null
   */
  public static Locale getLocale() {
    return localeInfo.get();
  }

}
