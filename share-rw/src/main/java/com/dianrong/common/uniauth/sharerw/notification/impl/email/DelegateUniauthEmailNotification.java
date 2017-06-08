package com.dianrong.common.uniauth.sharerw.notification.impl.email;

import com.dianrong.common.uniauth.sharerw.notification.EmailNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

/**
 * 代理具体的EmailNotify实现.
 *
 * @author wanglin
 */
@Slf4j
public class DelegateUniauthEmailNotification implements EmailNotification {

  private final EmailNotification emailNotify;

  /**
   * 根据配置configuration选择创建EmailNotify.
   *
   * @param config 配置
   * @param fromEmail 发送者的邮箱
   * @param smtpHost 邮箱服务器的host
   * @param smtpPort 邮箱服务器的port
   * @param notificationUserKey notification办法的userKey
   * @param notificationEndpoint notification服务器的地址
   */
  public DelegateUniauthEmailNotification(String config, String fromEmail, String smtpHost,
      int smtpPort, String notificationUserKey, String notificationEndpoint) {
    if (useNotificationNotify(config)) {
      log.info(String
          .format("apply SmsNotify implement is : %s", NotificationEmailNotify.class.getName()));
      NotificationEmailNotify notify = new NotificationEmailNotify(notificationUserKey,
          notificationEndpoint);
      if (StringUtils.hasText(fromEmail)) {
        notify.setFromEmail(fromEmail);
      }
      this.emailNotify = notify;
    } else {
      log.info(
          String.format("apply SmsNotify implement is : %s", SimpleEmailNotify.class.getName()));
      this.emailNotify = constructSimpleEmailNotify(fromEmail, smtpHost, smtpPort);
    }
  }

  @Override
  public void send(String subject, String toEmail, String notification) {
    this.emailNotify.send(subject, toEmail, notification);
  }

  // 构造SimpleEmailNotify
  private SimpleEmailNotify constructSimpleEmailNotify(String fromEmail, String smtpHost,
      int smtpPort) {
    SimpleEmailNotify emailNotify = new SimpleEmailNotify();
    if (StringUtils.hasText(fromEmail)) {
      emailNotify.setFromEmail(fromEmail);
    }
    if (StringUtils.hasText(smtpHost)) {
      emailNotify.setSmtpHost(smtpHost);
    }
    if (smtpPort > 0 && smtpPort < 65535) {
      emailNotify.setSmtpPort(smtpPort);
    }
    return emailNotify;
  }

  // 判断配置是决定采用哪一种实现方式
  private boolean useNotificationNotify(String config) {
    if ("true".equalsIgnoreCase(config)) {
      return true;
    }
    return false;
  }
}
