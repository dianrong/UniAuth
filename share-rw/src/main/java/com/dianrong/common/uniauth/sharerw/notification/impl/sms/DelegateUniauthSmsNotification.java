package com.dianrong.common.uniauth.sharerw.notification.impl.sms;

import com.dianrong.common.uniauth.sharerw.notification.SmsNotification;
import lombok.extern.slf4j.Slf4j;

/**
 * 根据配置决定使用默认实现还是notification的实现.
 *
 * @author wanglin
 */
@Slf4j
public class DelegateUniauthSmsNotification implements SmsNotification {

  // 被代理的实现
  private final SmsNotification smsNotify;

  /**
   * 构造一个Sms发送的实现.
   */
  public DelegateUniauthSmsNotification(String config, String notificationUserKey,
      String notificationEndpoint) {
    if (useNotificationNotify(config)) {
      log.info(String
          .format("apply SmsNotify implement is : %s", NotificationSmsNotify.class.getName()));
      this.smsNotify = new NotificationSmsNotify(notificationUserKey, notificationEndpoint);
    } else {
      log.info(String
          .format("apply SmsNotify implement is : %s", NotAvailableSmsNotify.class.getName()));
      this.smsNotify = new NotAvailableSmsNotify();
    }
  }

  @Override
  public void send(String phoneNumber, String notification) {
    this.smsNotify.send(phoneNumber, notification);
  }

  // 判断配置是决定采用哪一种实现方式
  private boolean useNotificationNotify(String config) {
    if ("true".equalsIgnoreCase(config)) {
      return true;
    }
    return false;
  }
}
