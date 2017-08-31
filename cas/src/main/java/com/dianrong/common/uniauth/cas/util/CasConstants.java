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

  /**
   * 用于指定登陆成功之后,跳转的目标地址.
   */
  String TARGET_URL_PARAMETER = "targetUrl";

  // request
  String REQUEST_PARAMETER_KEY_TENANCY_CODE = "tenancyCode";
  String REQUEST_PARAMETER_KEY_TENANCY_ID = "tenancyId";
  String REQUEST_PARAMETER_KEY_EMAIL = "email";
  String REQUEST_ATTRIBUTE_KEY_CREDENTIAL = "credential";
  String REQUEST_ATTRIBUTE_KEY_INITMSG = "initMsg";

  String LOGIN_SCROLL_IMAGES_MODEL_KEY = "loginImges";

  // session
  String CAS_CAPTCHA_SESSION_KEY = "CAS_CAPTCHA_SESSION_KEY";

  // tag for UserInfo edit in request
  String CAS_USERINFO_MANAGE_EDIT_KEY = "edituserinfo";
  String CAS_USERINFO_MANAGE_REQUEST_METHOD_KEY = "form_method";
  @Deprecated
  String CAS_USERINFO_MANAGE_FLOW_REQUEST_METHOD_TYPE_KEY = "user_mangage_flow_request_type";
  String CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG = "user_manage_errormsg";

  // 更新类型
  String CAS_USERINFO_MANAGE_UPDATE_METHOD_TYPE_TAG = "updatetype";

  // 用户信息获取
  String CAS_USERINFO_MANAGE_USER_ID_TAG = "id";
  String CAS_USERINFO_MANAGE_UPDATE_EMAIL_TAG = "email";
  String CAS_USERINFO_MANAGE_UPDATE_PHONE_TAG = "phone";
  String CAS_USERINFO_MANAGE_UPDATE_NAME_TAG = "name";
  String CAS_USERINFO_MANGE_UPDATE_PSWD_TAG = "password";
  String CAS_USERINFO_MANGE_UPDATE_ORIGINPSWD_TAG = "orign_password";

  String CAS_USER_IDENTITY = "user_identity";
  String CAS_USER_TENANCY_ID = "user_tenancy_id";

  // CAS登陆Captcha的session值的key
  String CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY = "cas_login_captcha_validation_session_key";

  // constants for password forget
  // dispatcher parameter
  String PSWDFORGET_DISPATCHER_STEP_KEY = "step";
  String PSWDFORGET_DISPATCHER_CONTEXTURL_KEY = "savedLoginContext";

  // cas cfg refresh
  // default 120 minutes
  long CAS_CFG_CACHE_REFRESH_PERIOD_MILLES = 120L * 60L * 1000L;

  // cascfg cache model in application's key
  String CAS_CFG_CACHE_MODEL_APPLICATION_KEY = "cascfg_cache_key";

  // cas cfg cfg_key list
  String CAS_CFG_KEY_LOGO = "CAS_LOGO";
  String CAS_CFG_KEY_ICON = "CAS_ICON";
  String CAS_CFG_KEY_TITLE = "CAS_TITLE";
  String CAS_CFG_KEY_ALL_RIGHT = "CAS_ALL_RIGHT";
  String CAS_CFG_KEY_LOGIN_AD_IMG = "CAS_LOGIN_AD_IMG";
  // cfg cas original cfg key prefix
  String CAS_CFG_KEY_CROSS_FILTER_ORIGIN_PREFIX = "CAS_CROSS_FILTER_ORGIN";

  // define the cfg type id
  String CAS_CFG_TYPE_FILE = "FILE";
  String CAS_CFG_TYPE_TEXT = "TEXT";

  // 首页广告对应herf的cfg key的后缀
  String CAS_CFG_LOGIN_AD_HREF_SUFFIX = "_HREF";
  // 默认定义的跳转url
  String CAS_CFG_HREF_DEFALT_VAL = "#";

  String CAS_CFG_KEY_BACKGROUND_COLOR = "CAS_BACKGROUND_COLOR";

  // login ticket prefix
  String CAS_LOGIN_TICKET_PREFIX = "LT";

  // 用户跳转的url路径
  String CAS_CUSTOM_LOGIN_PAGE_KEY = "custom_login_page_key";
  String CAS_CUSTOM_LOGIN_LT_KEY = "custom_login_lt_key";
  String CAS_CUSTOM_LOGIN_URL_KEY = "custom_login_url_key";

  // Application
  String SERVER_PROCESS_ERROR = "Server process error!";

  // login page
  String LOGIN_PAGE_REQUEST_CODE_KEY = "code";
}
