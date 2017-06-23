package com.dianrong.common.uniauth.sharerw.notification.impl.sms;

import com.dianrong.common.uniauth.sharerw.notification.SmsNotification;
import com.dianrong.common.uniauth.sharerw.notification.exp.NotificationNotAvailableException;

/**
 * Uniauth提供的一个默认短信发送实现.
 *
 * @author wanglin
 */
public class NotAvailableSmsNotify implements SmsNotification {

  @Override
  public void send(String phoneNumber, String notification) {
    throw new NotificationNotAvailableException();
  }

}
