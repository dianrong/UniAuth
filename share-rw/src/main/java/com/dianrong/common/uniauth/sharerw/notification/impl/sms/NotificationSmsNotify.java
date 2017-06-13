package com.dianrong.common.uniauth.sharerw.notification.impl.sms;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.sharerw.notification.SmsNotification;
import com.dianrong.common.uniauth.sharerw.notification.exp.SendNotificationFailedException;
import com.dianrong.platform.notification.http.ResponseMsg;
import com.dianrong.platform.notification.sms.SendSmsContentRequest;
import com.dianrong.platform.notification.sms.SmsHttpClient;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;

/**
 * 依赖notification的一个短信发送实现.
 *
 * @author wanglin
 */
@Slf4j
public class NotificationSmsNotify implements SmsNotification {

  // notification颁发的userKey
  private final String notificationUserKey;
  // notification的客户端
  private final SmsHttpClient smsClient;

  /**
   * 创建一个NotificationSmsNotify.
   *
   * @param notificationUserKey notification颁发的userKey
   * @param notificationEndpoint notification服务器的地址, 不能为空
   */
  public NotificationSmsNotify(String notificationUserKey, String notificationEndpoint) {
    Assert.notNull(notificationUserKey);
    Assert.notNull(notificationEndpoint);
    this.notificationUserKey = notificationUserKey;
    this.smsClient = new SmsHttpClient(notificationEndpoint);
  }

  @Override
  public void send(String phoneNumber, String notification) {
    Assert.notNull(phoneNumber);
    log.info(String.format("send sms to phone %s", phoneNumber));
    SendSmsContentRequest arg = new SendSmsContentRequest();
    HashSet<String> phoneSet = new HashSet<>(1);
    phoneSet.add(phoneNumber);
    arg.setCellphones(phoneSet);
    arg.setContent(notification);
    ResponseMsg<String> response = null;
    try {
      response = smsClient.sendContent(this.notificationUserKey, arg);
    } catch (Throwable t) {
      log.warn(
          String.format("failed send sms to %s, the content is:%s", phoneNumber, notification));
      throw new SendNotificationFailedException(String.format("failed send sms to %s", phoneNumber),
          t);
    }
    if (response == null || !response.isSuccess()) {
      log.warn(
          String.format("failed send sms to %s, the content is:%s", phoneNumber, notification));
      throw new SendNotificationFailedException(
          String.format("failed send sms to %s", phoneNumber));
    } else {
      log.info(String.format("success send sms to %s", phoneNumber));
    }
  }
}
