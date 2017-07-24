package com.dianrong.common.uniauth.server.util;

import com.dianrong.common.uniauth.common.server.UniauthLocaleInfoHolder;
import com.google.common.collect.Maps;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;

public class UniBundle {

  public static String getMsg(String key) {
    return ResourceBundlueHolder.getResource().getString(key);
  }

  /**
   * 根据国家化信息key获取国际化信息字符串.
   */
  public static String getMsg(String key, Object... arguments) {
    String raw = ResourceBundlueHolder.getResource().getString(key);
    String result = MessageFormat.format(raw, arguments);
    return result;
  }

  private static class ResourceBundlueHolder {

    private static final ConcurrentMap<Locale, ResourceBundle> RESOURCEBUNDLES =
        Maps.newConcurrentMap();

    public static ResourceBundle getResource() {
      Locale locale = UniauthLocaleInfoHolder.getLocale();
      ResourceBundle resourceBundle = RESOURCEBUNDLES.get(locale);
      if (resourceBundle == null) {
        RESOURCEBUNDLES.putIfAbsent(locale, ResourceBundle.getBundle("UniauthResource", locale));
      }
      return RESOURCEBUNDLES.get(locale);
    }
  }
}
