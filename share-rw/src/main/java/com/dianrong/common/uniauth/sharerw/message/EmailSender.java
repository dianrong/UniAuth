package com.dianrong.common.uniauth.sharerw.message;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Arc on 10/3/2016.
 */
@Slf4j
@Deprecated
public class EmailSender implements InitializingBean {

  // default values
  private static final String DEFAULTSMTPHOST = "smtp-dev.sl.com";
  private static final String DEFAULTFROMEMAIL = "TechOps-Notification<noreply@dianrong.com>";
  private static final int DEFAULTSMPTPORT = 25;
  // 10s
  private static final long EMAIL_CONNECTION_TIMEOUT = 10L * 1000L;
  private static final long EMAIL_READ_TIMEOUT = 10L * 1000L;

  @Value("#{uniauthConfig['internal.mail.smtp.host']}")
  private String internalSmtpHost;

  @Value("#{uniauthConfig['internal.mail.smtp.port']}")
  private String internalSmtpPort;

  @Value("#{uniauthConfig['internal.mail.smtp.femail']}")
  private String internalSmtpFromEmail;

  private String smtpHost;
  private String fromEmail;
  private int smtpPort;

  // init value
  @Override
  public void afterPropertiesSet() throws Exception {
    this.smtpHost = getConfig(internalSmtpHost, DEFAULTSMTPHOST);
    this.smtpPort = getConfig(internalSmtpPort, DEFAULTSMPTPORT);
    this.fromEmail = getConfig(internalSmtpFromEmail, DEFAULTFROMEMAIL);
  }

  private int getConfig(String config, int defaultVal) {
    if (StringUtil.strIsNullOrEmpty(config)) {
      return defaultVal;
    }
    Integer val = StringUtil.tryToTranslateStrToInt(config);
    if (val == null) {
      return defaultVal;
    }
    return val;
  }

  private String getConfig(String config, String defaultVal) {
    if (StringUtil.strIsNullOrEmpty(config)) {
      return defaultVal;
    }
    return config.trim();
  }

  /**
   * . 邮件发送线程池 初始化5个线程池
   */
  private static final ExecutorService executor = Executors.newFixedThreadPool(5);

  class EmailWorker implements Runnable {

    private String subject;
    private String toEmail;
    private StringBuffer buffer;

    public String getSubject() {
      return subject;
    }

    public EmailWorker setSubject(String subject) {
      this.subject = subject;
      return this;
    }

    public String getToEmail() {
      return toEmail;
    }

    public EmailWorker setToEmail(String toEmail) {
      this.toEmail = toEmail;
      return this;
    }

    public StringBuffer getBuffer() {
      return buffer;
    }

    public EmailWorker setBuffer(StringBuffer buffer) {
      this.buffer = buffer;
      return this;
    }

    @Override
    public void run() {
      try {
        log.debug("Sending email to : " + toEmail);
        log.debug("Content: \n " + buffer);
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host / mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        // props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.host", "smtp.sendcloud.net");
        // props.put("mail.smtp.port", "25");
        // 发件人的账号
        // props.put("mail.user", "postmaster@dianrong.sendcloud.org");
        // // 访问SMTP服务时需要提供的密码
        // props.put("mail.password", "mONh8xRTosRPJYC3");

        // 构建授权信息，用于进行SMTP进行身份验证
        // Authenticator authenticator = new Authenticator() {
        // @Override
        // protected PasswordAuthentication getPasswordAuthentication() {
        // // 用户名、密码
        // String userName = props.getProperty("mail.user");
        // String password = props.getProperty("mail.password");
        // return new PasswordAuthentication(userName, password);
        // }
        // };
        // // 使用环境属性和授权信息，创建邮件会话
        // Session mailSession = Session.getInstance(props, authenticator);
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
        message.setContent(buffer.toString(), "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
        log.debug("End of sending email to : " + toEmail);
        log.debug("Content: \n " + buffer);
      } catch (Exception e) {
        log.error("error to send email to the email:" + toEmail, e);
      }
    }
  }

  /**
   * 发送邮件.
   */
  public void sendEmail(String subject, String toEmail, StringBuffer buffer) {
    log.debug("Starting to asynchronous send email to : " + toEmail);
    EmailWorker emailWorker =
        new EmailWorker().setSubject(subject).setToEmail(toEmail).setBuffer(buffer);
    executor.submit(emailWorker);
    // new Thread(emailWorker).start();
    log.debug("End of asynchronous sending email to : " + toEmail);
    log.debug("Content: \n " + buffer);
  }
}
