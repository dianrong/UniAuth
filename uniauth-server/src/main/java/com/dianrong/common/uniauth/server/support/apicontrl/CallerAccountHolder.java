package com.dianrong.common.uniauth.server.support.apicontrl;

/**
 * Help class for saving request account per request.
 *
 * @author wanglin
 */
public final class CallerAccountHolder {

  private static final ThreadLocal<String> holder = new ThreadLocal<String>();

  /**
   * 添加.
   */
  public static void set(String val) {
    if (val == null) {
      holder.remove();
    }
    holder.set(val);
  }

  public static String get() {
    return holder.get();
  }

  public static void remove() {
    holder.remove();
  }
}
