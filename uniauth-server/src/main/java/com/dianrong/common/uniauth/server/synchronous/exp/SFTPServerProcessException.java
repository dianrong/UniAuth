package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * SFTP服务器相关操作失败.
 */
public class SFTPServerProcessException extends UniauthCommonException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * Define a empty method.
   */
  public SFTPServerProcessException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public SFTPServerProcessException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public SFTPServerProcessException(String msg, Throwable t) {
    super(msg, t);
  }
}
