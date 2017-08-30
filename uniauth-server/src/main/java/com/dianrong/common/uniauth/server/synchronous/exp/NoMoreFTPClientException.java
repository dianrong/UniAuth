package com.dianrong.common.uniauth.server.synchronous.exp;

/**
 * FTPClient池中没有更多的连接可用.
 */
public class NoMoreFTPClientException extends FTPServerProcessException{

  private static final long serialVersionUID = -2790987801666164932L;

  /**
   * Define a empty method.
   */
  public NoMoreFTPClientException() {
    super();
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public NoMoreFTPClientException(String msg) {
    super(msg);
  }

  /**
   * Define method for parameter msg.
   *
   * @param msg msg
   */
  public NoMoreFTPClientException(String msg, Throwable t) {
    super(msg, t);
  }
}
