package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 获取锁失败.
 */
public class AcquireLockFailureException extends UniauthCommonException{

  private static final long serialVersionUID = -2790987801666164932L;

  private String holdLockServerIp;
  /**
   * Define a empty method.
   */
  public AcquireLockFailureException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public AcquireLockFailureException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public AcquireLockFailureException(String msg, Throwable t) {
    super(msg, t);
  }

  public String getHoldLockServerIp() {
    return holdLockServerIp;
  }

  public void setHoldLockServerIp(String holdLockServerIp) {
    this.holdLockServerIp = holdLockServerIp;
  }
}
