package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * FTP服务器相关操作失败.
 */
public class FTPServerProcessException extends UniauthCommonException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * Define a empty method.
   */
  public FTPServerProcessException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public FTPServerProcessException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public FTPServerProcessException(String msg, Throwable t) {
    super(msg, t);
  }
}
