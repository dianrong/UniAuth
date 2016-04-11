package com.dianrong.common.uniauth.sharerw.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Arc on 10/3/2016.
 */
public class EmailSender {
    private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

    @Value("#{uniauthConfig['internal.mail.smtp.host']}")
    private String internalSmtpHost;

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
                logger.debug("Sending email to : " + toEmail);
                logger.debug("Content: \n " + buffer);
                // 配置发送邮件的环境属性
                final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
                // 表示SMTP发送邮件，需要进行身份验证
//                props.put("mail.smtp.auth", "true");
//                props.put("mail.smtp.host", "smtp.sendcloud.net");
//                props.put("mail.smtp.port", "25");
                // 发件人的账号
//                props.put("mail.user", "postmaster@dianrong.sendcloud.org");
//                // 访问SMTP服务时需要提供的密码
//                props.put("mail.password", "mONh8xRTosRPJYC3");

                // 构建授权信息，用于进行SMTP进行身份验证
//                Authenticator authenticator = new Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        // 用户名、密码
//                        String userName = props.getProperty("mail.user");
//                        String password = props.getProperty("mail.password");
//                        return new PasswordAuthentication(userName, password);
//                    }
//                };
//                // 使用环境属性和授权信息，创建邮件会话
//                Session mailSession = Session.getInstance(props, authenticator);
                String host;
                if(internalSmtpHost != null) {
                    host = internalSmtpHost;
                } else {
                    host = "smtp-dev.sl.com";
                }

                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", "25");
                props.put("mail.user", "TechOps-Notification<noreply@dianrong.com>");
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
                logger.debug("End of sending email to : " + toEmail);
                logger.debug("Content: \n " + buffer);
            } catch (Exception e) {
                logger.error("error to send email to the email:" + toEmail, e);
            }
        }
    }

    public void sendEmail(String subject, String toEmail, StringBuffer buffer) {
        logger.debug("Starting to asynchronous send email to : " + toEmail);
        EmailWorker emailWorker = new EmailWorker().setSubject(subject).setToEmail(toEmail).setBuffer(buffer);
        new Thread(emailWorker).start();
        logger.debug("End of asynchronous sending email to : " + toEmail);
        logger.debug("Content: \n " + buffer);
    }

}
