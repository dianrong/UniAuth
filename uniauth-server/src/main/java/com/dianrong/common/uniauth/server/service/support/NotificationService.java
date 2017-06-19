package com.dianrong.common.uniauth.server.service.support;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.sharerw.notification.EmailNotification;
import com.dianrong.common.uniauth.sharerw.notification.SmsNotification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用于消息发送的service, 比如:修改密码通知,新建用户通知.
 * 
 * @author wanglin
 */
@Slf4j
@Service
public class NotificationService {
  /**
   * 发送消息线程池.
   */
  private static final ExecutorService NOTIFICATION_EXECUTOR_SERVICE =
      Executors.newFixedThreadPool(2);

  /**
   * 消息标题.
   */
  private static final String NOTIFICATION_TITLE = "内部账号系统通知";

  /**
   * cas server的地址.
   */
  // @Value("#{uniauthConfig['cas_server']}")
  private String casServerUrl;

  @Value("#{uniauthConfig['domains.techops.email_switch']}")
  private String emailSwitch;

  /**
   * 短信发送.
   */
  @Autowired
  private SmsNotification smsNotify;

  /**
   * 邮件发送.
   */
  @Autowired
  private EmailNotification emailNotify;

  // 新增用户的邮件template
  private String addUserNotTemplate;
  // 新增用户的短信模板
  private String addUserSimpleNotTemplate;

  // 更新用户密码的邮件的template
  private String updateUserPwdNotTemplate;
  // 更新密码的短信发送模板
  private String updateUserPwdSimpleNotTemplate;

  /**
   * 初始化消息通知的模板.
   */
  public NotificationService() {}

  // 初始化消息发送相关的模板
  @PostConstruct
  private void initTemplate() {
    // add user notification template
    StringBuilder addUserEmailTemplate = new StringBuilder();
    addUserEmailTemplate.append("====================================================<br />");
    addUserEmailTemplate.append("            ");
    addUserEmailTemplate.append("     系统管理员为您创建了系统账户<br />");
    addUserEmailTemplate.append("            ");
    addUserEmailTemplate.append(" 您的登录账号为: %s        <br />");
    addUserEmailTemplate.append("            ");
    addUserEmailTemplate.append(" 您的账户临时密码为: %s        <br />");
    if (casServerUrl != null) {
      addUserEmailTemplate.append("            ");
      addUserEmailTemplate.append(" 请到: " + casServerUrl + " 去登陆       <br />");
    }
    addUserEmailTemplate.append("====================================================<br />");
    addUserNotTemplate = addUserEmailTemplate.toString();
    // sms
    StringBuilder addUserSmsTemplate = new StringBuilder();
    addUserSmsTemplate.append("系统管理员为您创建了系统账户!您的登录账号为:%s,临时登陆密码为:%s");
    if (casServerUrl != null) {
      addUserSmsTemplate.append(" 请到: " + casServerUrl + " 去登陆");
    }
    addUserSimpleNotTemplate = addUserSmsTemplate.toString();

    // update user password notification template
    StringBuilder updateUserPwdEmailTemplate = new StringBuilder();
    updateUserPwdEmailTemplate.append("====================================================<br />");
    updateUserPwdEmailTemplate.append("            ");
    updateUserPwdEmailTemplate.append("      系统管理员重置了您的系统账户密码<br />");
    updateUserPwdEmailTemplate.append("            ");
    updateUserPwdEmailTemplate.append(" 您的登录账号为: %s        <br />");
    updateUserPwdEmailTemplate.append("            ");
    updateUserPwdEmailTemplate.append(" 您的账户临时密码为: %s        <br />");
    if (casServerUrl != null) {
      updateUserPwdEmailTemplate.append("            ");
      updateUserPwdEmailTemplate.append("请到: " + casServerUrl + " 去登陆       <br />");
    }
    updateUserPwdEmailTemplate.append("====================================================<br />");
    updateUserPwdNotTemplate = updateUserPwdEmailTemplate.toString();
    // sms
    StringBuilder updateUserPwdSmsTemplate = new StringBuilder();
    updateUserPwdSmsTemplate.append("系统管理员重置了您的系统账户密码!您的登录账号为:%s,临时登陆密码为:%s");
    if (casServerUrl != null) {
      updateUserPwdSmsTemplate.append(" 请到: " + casServerUrl + " 去登陆");
    }
    updateUserPwdSimpleNotTemplate = updateUserPwdSmsTemplate.toString();
  }

  /**
   * 新增用户之后发送通知.
   * 
   * @param userInfo UserDto 新增用户的信息,不能为空
   */
  public void addUserNotification(final User userInfo) {
    Assert.notNull(userInfo);
    NOTIFICATION_EXECUTOR_SERVICE.execute(new Runnable() {
      @Override
      public void run() {
        if (notificationIsOn()) {
          if (StringUtils.hasText(userInfo.getEmail())) {
            try {
              String emailContent =
                  String.format(addUserNotTemplate, userInfo.getEmail(), userInfo.getPassword());
              emailNotify.send(NOTIFICATION_TITLE, userInfo.getEmail(), emailContent);
            } catch (Exception ex) {
              log.error("failed to send email ", ex);
            }
          }
          // send sms
          if (StringUtils.hasText(userInfo.getPhone())) {
            try {
              String smsContent = String.format(addUserSimpleNotTemplate, userInfo.getPhone(),
                  userInfo.getPassword());
              smsNotify.send(userInfo.getPhone(), smsContent);
            } catch (Exception ex) {
              log.error("failed to send sms ", ex);
            }
          }
        }
      }
    });
  }

  /**
   * 更新用户密码之后发送消息给用户.
   * 
   * @param userInfo UserDto 更新用户密码的用户信息
   */
  public void updateUserPwdNotification(final User userInfo) {
    Assert.notNull(userInfo);
    NOTIFICATION_EXECUTOR_SERVICE.execute(new Runnable() {
      @Override
      public void run() {
        if (notificationIsOn()) {
          if (StringUtils.hasText(userInfo.getEmail())) {
            try {
              String emailContent = String.format(updateUserPwdNotTemplate, userInfo.getEmail(),
                  userInfo.getPassword());
              emailNotify.send(NOTIFICATION_TITLE, userInfo.getEmail(), emailContent);
            } catch (Exception ex) {
              log.error("failed to send email ", ex);
            }
          }
          // send sms
          if (StringUtils.hasText(userInfo.getPhone())) {
            try {
              String smsContent = String.format(updateUserPwdSimpleNotTemplate, userInfo.getPhone(),
                  userInfo.getPassword());
              smsNotify.send(userInfo.getPhone(), smsContent);
            } catch (Exception ex) {
              log.error("failed to send sms ", ex);
            }
          }
        }
      }
    });
  }

  // 消息发送开关
  private boolean notificationIsOn() {
    boolean isOn = !Boolean.FALSE.toString().equalsIgnoreCase(this.emailSwitch);
    if (isOn) {
      log.debug("notification switch is on");
    } else {
      log.debug("notification switch is off");
    }
    return isOn;
  }
}
