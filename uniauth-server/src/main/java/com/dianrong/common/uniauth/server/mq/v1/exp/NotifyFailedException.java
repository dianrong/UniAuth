package com.dianrong.common.uniauth.server.mq.v1.exp;

/**
 * 通知消息发送失败的异常定义.
 *
 * @author wanglin
 */
public class NotifyFailedException extends RuntimeException {

  private static final long serialVersionUID = 6077556512276315195L;

  public NotifyFailedException() {
    super();
  }

  public NotifyFailedException(String msg) {
    super(msg);
  }

  public NotifyFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
