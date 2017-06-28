package com.dianrong.common.uniauth.common.enm;

public enum UserActionEnum {
  LOCK,
  UNLOCK,
  STATUS_CHANGE,
  RESET_PASSWORD,
  UPDATE_INFO,
  RESET_PASSWORD_AND_CHECK,
  UPDATE_INFO_BY_ACCOUNT,
  UPDATE_EMAIL_BY_ACCOUNT,
  UPDATE_PHONE_BY_ACCOUNT,
  UPDATE_PASSWORD_BY_ACCOUNT,;

  /**
   * Check the action is update info by account.
   *
   * @param action UserActionEnum
   * @return true or false
   */
  public static boolean isUpdateByAccount(UserActionEnum action) {
    switch (action) {
      case UPDATE_INFO_BY_ACCOUNT:
        return true;
      case UPDATE_EMAIL_BY_ACCOUNT:
        return true;
      case UPDATE_PHONE_BY_ACCOUNT:
        return true;
      case UPDATE_PASSWORD_BY_ACCOUNT:
        return true;
      default:
        break;
    }
    return false;
  }
    
  /**
   * 判断是否在修改密码.
   */
  public static boolean isPasswordChange(UserActionEnum action) {
    return isUpdatePwdAdmin(action) || isUpdatePwdSelf(action);
  }
  
  /**
   * 管理员重置密码.
   */
  public static boolean isUpdatePwdAdmin(UserActionEnum action) {
    switch (action) {
      case RESET_PASSWORD:
        return true;
      default:
        break;
    }
    return false;
  }
  
  /**
   * 自己修改密码.
   */
  public static boolean isUpdatePwdSelf(UserActionEnum action) {
    switch (action) {
      case RESET_PASSWORD_AND_CHECK:
        return true;
      case UPDATE_PASSWORD_BY_ACCOUNT:
        return true;
      default:
        break;
    }
    return false;
  }
}
