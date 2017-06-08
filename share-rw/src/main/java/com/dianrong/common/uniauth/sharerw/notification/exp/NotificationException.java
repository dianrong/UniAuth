package com.dianrong.common.uniauth.sharerw.notification.exp;

/**
 * 发送消息类异常信息.
 *
 * @author wanglin
 */
public class NotificationException extends RuntimeException {

  private static final long serialVersionUID = 2893220117279756397L;

  public NotificationException() {
    super();
  }

  public NotificationException(String msg) {
    super(msg);
  }

  public NotificationException(String msg, Throwable t) {
    super(msg, t);
  }
}
