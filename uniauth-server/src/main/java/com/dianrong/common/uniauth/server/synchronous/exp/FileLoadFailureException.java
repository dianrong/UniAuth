package com.dianrong.common.uniauth.server.synchronous.exp;

import com.dianrong.common.uniauth.common.exp.UniauthCommonException;

/**
 * 加载同步文件失败异常.
 */
public class FileLoadFailureException extends UniauthCommonException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * Define a empty method.
   */
  public FileLoadFailureException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public FileLoadFailureException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public FileLoadFailureException(String msg, Throwable t) {
    super(msg, t);
  }
}
