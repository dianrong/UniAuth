package com.dianrong.common.uniauth.sharerw.notification;

import com.dianrong.common.uniauth.sharerw.notification.exp.NotificationNotAvailableException;

/**
 * 发送短信的接口.
 *
 * @author wanglin
 */
public interface SmsNotification extends Notification {

  /**
   * 发短信.
   *
   * @param phoneNumber 接收短信人的号码
   * @throws NotificationNotAvailableException 当前消息发送接口不可用
   * @throws SendNotificationFailedException 如果消息发送失败
   */
  void send(String phoneNumber, String notification);
}
