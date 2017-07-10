package com.dianrong.common.uniauth.server.service.common.notify;

/**
 * 管理消息通知的各种模板.
 */
public interface NotifyTemplate {
  /**
   * 获取邮件发送的标题.
   */
  String getEmailTitle(Object... args);
  
  /**
   * 获取短信发送的标题.
   */
  String getSmsTitle(Object... args);
  
  /**
   * 获取邮件发送内容.
   * 参数应该为: account, password,casUrl
   */
  String getEmailNotifyMsg(Object... args);

  /**
   * 获取短信发送内容.
   * 参数应该为: account, password,casUrl
   */
  String getSmsNotifyMsg(Object... args);
}
