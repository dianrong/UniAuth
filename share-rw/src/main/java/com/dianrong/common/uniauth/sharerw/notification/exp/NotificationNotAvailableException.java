package com.dianrong.common.uniauth.sharerw.notification.exp;

public class NotificationNotAvailableException extends NotificationException {

  private static final long serialVersionUID = -3783277420226439420L;

  public NotificationNotAvailableException() {
    super();
  }

  public NotificationNotAvailableException(String msg) {
    super(msg);
  }

  public NotificationNotAvailableException(String msg, Throwable t) {
    super(msg, t);
  }
}
