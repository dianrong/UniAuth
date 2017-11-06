package com.dianrong.common.uniauth.cas.helper;

/**
 * 辅助类, 如果用户没有员工标号,则将该用户的id存储到ThreadLocal中.
 */
public class StaffNoPersistTagHolder {
  private static final ThreadLocal<Long> PERSIST_STAFF_NO = new ThreadLocal<Long>();

  public static Long get(){
    return PERSIST_STAFF_NO.get();
  }

  public static void persist(Long userId){
    if (userId == null) {
      return;
    }
    PERSIST_STAFF_NO.set(userId);
  }

  public static void remove(){
    PERSIST_STAFF_NO.remove();
  }
}
