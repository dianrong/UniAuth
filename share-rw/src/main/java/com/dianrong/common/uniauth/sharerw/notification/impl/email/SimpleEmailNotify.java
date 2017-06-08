package com.dianrong.common.uniauth.sharerw.notification.impl.email;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.sharerw.notification.EmailNotification;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * 简单的邮件发送实现.
 *
 * @author wanglin
 */
@Slf4j
public class SimpleEmailNotify implements EmailNotification {

  // default values
  public static final String DEFAULT_SMTP_HOST = "smtp-dev.sl.com";
  public static final String DEFAULT_FROM_EMAIL = "TechOps-Notification<noreply@dianrong.com>";
  public static final int DEFAULT_SMPT_PORT = 25;
  // 10s
  private static final long EMAIL_CONNECTION_TIMEOUT = 10L * 1000L;
  private static final long EMAIL_READ_TIMEOUT = 10L * 1000L;

  // 邮件发送线程池
  private static final ExecutorService executor = Executors.newFixedThreadPool(2);

  // 邮件服务器host
  private String smtpHost = DEFAULT_SMTP_HOST;
  // 邮件服务器port
  private int smtpPort = DEFAULT_SMPT_PORT;
  // 邮件发送人
  private String fromEmail = DEFAULT_FROM_EMAIL;

  /**
   * 设置邮件服务器的Host.
   */
  public void setSmtpHost(String smtpHost) {
    Assert.notNull(smtpHost);
    log.info("set email server host : " + smtpHost);
    this.smtpHost = smtpHost;
  }

  /**
   * 设置邮件服务器的Port.
   */
  public void setSmtpPort(int smtpPort) {
    if (smtpPort <= 0 || smtpPort >= 65535) {
      throw new IllegalArgumentException("invalid smtpPort set: " + smtpPort);
    }
    log.info("set email server port : " + smtpPort);
    this.smtpPort = smtpPort;
  }

  /**
   * 设置发件人邮箱.
   */
  public void setFromEmail(String fromEmail) {
    Assert.notNull(fromEmail);
    log.info("set email send from email : " + fromEmail);
    this.fromEmail = fromEmail;
  }

  // 邮件发送任务
  private class EmailWorker implements Runnable {

    private String subject;
    private String toEmail;
    private String content;

    public EmailWorker setSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public EmailWorker setToEmail(String toEmail) {
      this.toEmail = toEmail;
      return this;
    }

    public EmailWorker setContent(String content) {
      this.content = content;
      return this;
    }

    @Override
    public void run() {
      try {
        log.debug("Sending email to : " + toEmail);
        log.debug("Content: \n " + content);
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.user", fromEmail);

        props.put("mail.smtp.connectiontimeout", EMAIL_CONNECTION_TIMEOUT);
        props.put("mail.smtp.timeout", EMAIL_READ_TIMEOUT);

        Session mailSession = Session.getInstance(props);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress from = new InternetAddress(props.getProperty("mail.user"));
        message.setFrom(from);

        // 设置收件人
        InternetAddress to = new InternetAddress(toEmail);
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject(AppConstants.MAIL_PREFIX + subject);

        // 设置邮件的内容体
        message.setContent(content, "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
        log.debug("End of sending email to : " + toEmail);
        log.debug("Content: \n " + content);
      } catch (Exception e) {
        log.error("error to send email to the email:" + toEmail, e);
      }
    }
  }

  @Override
  public void send(String subject, String toEmail, String notification) {
    log.debug("Starting to asynchronous send email to : " + toEmail);
    EmailWorker emailWorker = new EmailWorker().setSubject(subject).setToEmail(toEmail)
        .setContent(notification);
    executor.submit(emailWorker);
    log.debug("End of asynchronous sending email to : " + toEmail);
    log.debug("Content: \n " + notification);
  }
}
