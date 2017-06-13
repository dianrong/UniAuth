package com.dianrong.common.uniauth.sharerw.notification;

import com.dianrong.common.uniauth.sharerw.notification.exp.NotificationNotAvailableException;
import com.dianrong.common.uniauth.sharerw.notification.exp.SendNotificationFailedException;

/**
 * 发送邮件的接口.
 *
 * @author wanglin
 */
public interface EmailNotification extends Notification {

  /**
   * 发送通知消息.
   *
   * @param subject 发送的主题
   * @param toEmail 收邮件的地址,不能为空
   * @param notification 通知内容
   * @throws NotificationNotAvailableException 当前消息发送接口不可用
   * @throws SendNotificationFailedException 如果消息发送失败
   */
  void send(String subject, String toEmail, String notification);
}
