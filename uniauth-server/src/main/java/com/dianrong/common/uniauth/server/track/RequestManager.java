package com.dianrong.common.uniauth.server.track;

public class RequestManager {

  private static ThreadLocal<GlobalVar> threadLocal = new ThreadLocal<GlobalVar>();

  private RequestManager() {

  }

  public static GlobalVar getGlobalVar() {
    return threadLocal.get();
  }

  /**
   * set.
   */
  public static void setGlobalVar(GlobalVar globalVar) {
    if (globalVar != null && getGlobalVar() == null) {
      threadLocal.set(globalVar);
    }
  }

  /**
   * clear.
   */
  public static void closeRequest() {
    if (getGlobalVar() != null) {
      threadLocal.remove();
    }
  }
}
