package com.dianrong.common.uniauth.server.service.common.notify;

import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.sharerw.notification.EmailNotification;
import com.dianrong.common.uniauth.sharerw.notification.SmsNotification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
   * CAS server的地址.
   */
  @Value("#{uniauthConfig['cas_server']}")
  private String casServerUrl;

  @Value("#{uniauthConfig['domains.techops.email_switch']}")
  private String emailSwitch;

  /**
   * 邮件发送.
   */
  @Autowired
  private EmailNotification emailNotify;

  /**
   * 短信发送.
   */
  @Autowired
  private SmsNotification smsNotify;

  /**
   * 发起通知.
   * 
   * @param userInfo UserDto 新增用户的信息,不能为空
   */
  public void notify(final User userInfo, final String purePassword, NotifyType type) {
    Assert.notNull(userInfo);
    Assert.notNull(type);
    NotifyParam param = new NotifyParam();
    param.setEmail(userInfo.getEmail());
    param.setPhone(userInfo.getPhone());
    param.setEmailTitle(NotifyTemplates.getEmailTitle(type));
    param.setEmailContent(
        NotifyTemplates.getEmailMsg(type, userInfo.getEmail(), purePassword, this.casServerUrl));
    param.setSmsContent(
        NotifyTemplates.getSmsMsg(type, userInfo.getPhone(), purePassword, this.casServerUrl));
    notification(param);
  }

  /**
   * 消息通知实现.
   */
  private void notification(final NotifyParam param) {
    NOTIFICATION_EXECUTOR_SERVICE.execute(new Runnable() {
      @Override
      public void run() {
        if (notificationIsOn()) {
          if (StringUtils.hasText(param.getEmail())) {
            try {
              emailNotify.send(param.getEmailTitle(), param.getEmail(), param.getEmailContent());
            } catch (Exception ex) {
              log.error("failed to send email ", ex);
            }
          }
          // send sms
          if (StringUtils.hasText(param.getPhone())) {
            try {
              smsNotify.send(param.getPhone(), param.getSmsContent());
            } catch (Exception ex) {
              log.error("failed to send sms ", ex);
            }
          }
        }
      }
    });
  }

  /**
   * 用于通知传参.
   */
  @ToString
  @Getter
  @Setter
  private static class NotifyParam {
    private String email;
    private String phone;
    private String emailTitle;
    private String emailContent;
    private String smsContent;
  }

  // 消息发送开关
  private boolean notificationIsOn() {
    boolean isOn = !Boolean.FALSE.toString().equalsIgnoreCase(this.emailSwitch);
    if (isOn) {
      log.info("notification switch is on");
    } else {
      log.info("notification switch is off");
    }
    return isOn;
  }
}
