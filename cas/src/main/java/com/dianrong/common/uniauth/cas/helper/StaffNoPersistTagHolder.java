package com.dianrong.common.uniauth.cas.helper;

/**
 * 辅助类, 标识是否需要进行员工编号编辑.
 */
public final class StaffNoPersistTagHolder {

  private static final ThreadLocal<Boolean> PERSIST_STAFF_NO = new ThreadLocal<Boolean>() {
    protected Boolean initialValue() {
      return Boolean.FALSE;
    }
  };

  public static Boolean get() {
    return PERSIST_STAFF_NO.get();
  }

  public static void persist() {
    PERSIST_STAFF_NO.set(Boolean.TRUE);
  }

  public static void remove() {
    PERSIST_STAFF_NO.remove();
  }
}
