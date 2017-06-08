package com.dianrong.common.uniauth.sharerw.notification.exp;

/**
 * 发送消息失败.
 *
 * @author wanglin
 */
public class SendNotificationFailedException extends NotificationException {

  private static final long serialVersionUID = -3783277420226439420L;

  public SendNotificationFailedException() {
    super();
  }

  public SendNotificationFailedException(String msg) {
    super(msg);
  }

  public SendNotificationFailedException(String msg, Throwable t) {
    super(msg, t);
  }
}
