package com.dianrong.common.uniauth.sharerw.notification.impl.email;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.sharerw.notification.EmailNotification;
import com.dianrong.common.uniauth.sharerw.notification.exp.SendNotificationFailedException;
import com.dianrong.platform.notification.email.EmailHttpClient;
import com.dianrong.platform.notification.email.SendEmailRequest;
import com.dianrong.platform.notification.http.ResponseMsg;
import lombok.extern.slf4j.Slf4j;

/**
 * 依赖点融的notification的一个实现.
 *
 * @author wanglin
 */
@Slf4j
public class NotificationEmailNotify implements EmailNotification {

  // default email sender
  private static final String DEFAULT_FROM_EMAIL = "noreply@dianrong.com";
  private static final String DEFAULT_FROM_EMAIL_NAME = "TechOps-Notification";
  // 邮件发送邮箱
  private String fromEmail = DEFAULT_FROM_EMAIL;

  // notification颁发的userKey
  private final String notificationUserKey;

  // notification的邮件发送客户端
  private final EmailHttpClient emailClient;

  /**
   * 构造一个NotificationEmailNotify.
   *
   * @param notificationUserKey notification颁发的一个userKey，不能为空
   * @param notificationEndpoint notification服务器的地址, 不能为空
   */
  public NotificationEmailNotify(String notificationUserKey, String notificationEndpoint) {
    Assert.notNull(notificationUserKey);
    Assert.notNull(notificationEndpoint);
    this.notificationUserKey = notificationUserKey;
    this.emailClient = new EmailHttpClient(notificationEndpoint);
  }

  /**
   * 自定义发送邮件邮箱.
   *
   * @param fromEmail 发送邮箱
   */
  public void setFromEmail(String fromEmail) {
    Assert.notNull(fromEmail);
    this.fromEmail = fromEmail;
  }

  @Override
  public void send(String subject, String toEmail, String notification) {
    Assert.notNull(toEmail);
    log.info(String.format("send email to %s , subject is %s ", toEmail, subject));
    SendEmailRequest arg = new SendEmailRequest();
    arg.setTo(toEmail);
    arg.setSubject(subject);
    arg.setFrom(this.fromEmail);
    arg.setFromName(DEFAULT_FROM_EMAIL_NAME);
    arg.setText(notification);
    ResponseMsg<String> response = null;
    try {
      response = emailClient.send(this.notificationUserKey, arg);
    } catch (Throwable t) {
      log.warn(String
          .format("failed send email, from %s to %s, the subject is %s", this.fromEmail, toEmail,
              subject));
      throw new SendNotificationFailedException(
          String.format("failed send email, from %s to %s, the subject is %s", this.fromEmail,
              toEmail, subject), t);
    }
    if (response != null && response.isSuccess()) {
      log.info(String
          .format("success send email, from %s to %s, subject is %s", this.fromEmail, toEmail,
              subject));
    } else {
      log.warn(String
          .format("failed send email, from %s to %s, the subject is %s", this.fromEmail, toEmail,
              subject));
      throw new SendNotificationFailedException(
          String.format("failed send email, from %s to %s, the subject is %s", this.fromEmail,
              toEmail, subject));
    }
  }
}
