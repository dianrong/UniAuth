package com.dianrong.common.uniauth.cas.util;

/**
 * CAS 用到的常量.
 *
 * @author wanglin
 */
public interface CasConstants {

  // captcha
  String CAPTCHA_SESSION_KEY = "CAS_CAPTCHA_SESSION_KEY";

  // sms verification
  String SMS_VERIFICATION_SESSION_KEY = "SMS_VERIFICATION_SESSION_KEY";

  // email verification
  String EMAIL_VERIFICATION_SESSION_KEY = "EMAIL_VERIFICATION_SESSION_KEY";

  // 1 hour
  long VERIFICATION_EXPIRED_MINUTES = 60L;
  long VERIFICATION_EXPIRED_MILLES = VERIFICATION_EXPIRED_MINUTES * 60L * 1000L;

  String PSWDFORGET_MAIL_VAL_KEY = "pwdg_emailVal";
  String PSWDFORGET_TENAYC_ID_KEY = "pwdg_tenancyId";

  /**
   * 用于指定登陆成功之后,跳转的目标地址.
   */
  public static final String TARGET_URL_PARAMETER = "targetUrl";
}
