package com.dianrong.common.uniauth.server.service.common.notify;

/**
 * Notify的类型.
 */
public enum NotifyType {
  /**
   * 添加用户通知.
   */
  ADD_USER,
  
  /**
   * 系统管理员更新密码通知.   
   */
  UPDATE_PSWD_ADMIN,
  
  /**
   * 自己修改密码通知.
   */
  UPDATE_PSWD_SELF;
}
