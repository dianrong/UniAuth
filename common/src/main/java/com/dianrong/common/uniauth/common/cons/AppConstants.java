package com.dianrong.common.uniauth.common.cons;

/**
 * Created by Arc on 26/1/16.
 */
public interface AppConstants {
  // 0 = 启用
  Byte ZERO_BYTE = (byte) 0;
  // 1 = 禁用
  Byte ONE_BYTE = (byte) 1;

  // 类型0
  Byte ZERO_TYPE = ZERO_BYTE;
  // 类型1
  Byte ONE_TYPE = ONE_BYTE;

  // 0 = 启用
  byte ZERO_BYTE_PRIMITIVE = (byte) 0;
  // 1 = 禁用
  byte ONE_BYTE_PRIMITIVE = (byte) 1;

  // 人性化设计
  byte STATUS_ENABLED = ZERO_BYTE_PRIMITIVE;
  byte STATUS_DISABLED = ONE_BYTE_PRIMITIVE;

  String API_UUID = "api-uuid";

  String NODE_TYPE_GROUP = "grp";
  String NODE_TYPE_MEMBER_USER = "mUser";
  String NODE_TYPE_OWNER_USER = "oUser";

  String GRP_ROOT = "GRP_ROOT";
  String TECHOPS_SUPER_ADMIN_GRP = "GRP_TECHOPS_SUPER_ADMIN";

  byte MAX_AUTH_FAIL_COUNT = 10;
  int MAX_PASSWORD_VALID_MONTH = 6;
  int DUPLICATE_PWD_VALID_MONTH = 8;
  // this value can avoid the "data.query.result.number.exceed" exception when you need query as
  // much as more.
  Integer MAX_PAGE_SIZE_MINUS_ONE = 4999;
  // if you don't pass pageSize parameter to the query api, it is the default value.
  Integer MAX_PAGE_SIZE = 5000;
  // if you don't pass pageNumber parameter to the query api, it is the default value.
  Integer MIN_PAGE_NUMBER = 0;
  String PERM_TYPE_DOMAIN = "DOMAIN";
  String PERM_TYPE_URIPATTERN = "URI_PATTERN";
  String PERM_TYPE_PRIVILEGE = "PRIVILEGE";
  String PERM_TYPE_THIRD_ACCOUNT = "THIRD_ACCOUNT";
  String DOMAIN_CODE_TECHOPS = "techops";

  // ROLE_NORMAL
  String ROLE_CODE_ROLE_NORMAL = "ROLE_NORMAL";

  /*** zk节点配置相关的常量定义. */
  /**
   * zk配置中的分隔符.
   */
  String ZK_CFG_SPLIT = ".";

  /**
   * domain.
   */
  String ZK_DOMAIN = "domains";
  /**
   * zk中domain节点的前缀.
   */
  String ZK_DOMAIN_PREFIX = ZK_DOMAIN + ZK_CFG_SPLIT;
  /**
   * 域的登陆页属性.
   */
  String ZK_DOMAIN_LOGIN_PAGE = "loginPage";
  /**
   * ticket 验证失败url.
   **/
  String ZK_DOMAIN_AUTH_FAIL_URL = "auth_fail_url";
  /**
   * 是否在主页显示登陆项.
   */
  String ZK_DOMAIN_SHOW_IN_HOME_PAGE = "showInHomePage";
  /**
   * zk节点中,域的登出地址的节点后缀.
   */
  String ZK_DOMAIN_LOGOUT_ADDRESS_NODE_SUFFIX = "logout_address";
  /**
   * 定义域的所属tenancy的code.
   */
  String ZK_DOMAIN_TEANANCY_CODE = "tenancycode";
  /**
   * zk 配置的node name， st使用的次数.
   */
  String ZK_NODE_NAME_ST_USED_TIMES = "cas.st_use_times";
  /**
   * zk 配置cas的短信和邮箱验证码的显示开关.
   */
  String ZK_CAS_VERIFY_CODE_SHOW = "cas.verify_code.show";


  String SERVICE_LOGIN_SUFFIX = "/login/cas";

  String ROLE_ADMIN = "ROLE_ADMIN";
  String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

  String CROSS_RESOURCE_ORIGIN_HEADER = "Origin";
  String AJAX_HEADER = "X-Requested-With";
  String JQUERY_XMLHttpRequest_HEADER = "XMLHttpRequest";
  String LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";
  String NO_PRIVILEGE = "NO_PRIVILEGE";

  String PERM_TYPE_DOMAIN_ID = "DOMAIN_ID";
  String CFG_TYPE_TEXT = "TEXT";
  String CFG_TYPE_FILE = "FILE";


  // constants for password forget

  // dispatcher parameter
  String PSWDFORGET_DISPATCHER_STEP_KEY = "step";
  String PSWDFORGET_DISPATCHER_CONTEXTURL_KEY = "savedLoginContext";
  String CAS_CAPTCHA_SESSION_TYPE_KEY = "captchaType";

  // request
  String REQUEST_PARAMETER_KEY_TENANCY_CODE = "tenancyCode";
  String REQUEST_PARAMETER_KEY_TENANCY_ID = "tenancyId";
  String REQUEST_PARAMETER_KEY_EMAIL = "email";
  String REQUEST_ATTRIBUTE_KEY_CREDENTIAL = "credential";
  String REQUEST_ATTRIBUTE_KEY_INITMSG = "initMsg";

  String PSWDFORGET_PAGE_VERIFY_CODE_CLIENT_KEY = "pageVerifyCode";
  String PSWDFORGET_MAIL_VAL_CLIENT_KEY = "email";
  String PSWDFORGET_MAIL_VERIFY_CODE_CLIENT_KEY = "verifyCode";
  String PSWDFORGET_NEW_PSWD_KEY = "newPassword";
  String LOGIN_SCROLL_IMAGES_MODEL_KEY = "loginImges";

  // session
  String CAS_CAPTCHA_SESSION_KEY = "CAS_CAPTCHA_SESSION_KEY";
  String PSWDFORGET_MAIL_VAL_KEY = "pwdg_emailVal";
  String PSWDFORGET_TENAYC_ID_KEY = "pwdg_tenancyId";
  /**
   * useless after integrate notification and challenge.
   */
  @Deprecated
  String PSWDFORGET_MAIL_VERIFY_CODE_KEY = "pwdg_verifyCode";
  String PSWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY = "pwdg_savedLoginContext";
  String PSWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY = "pwdg_verifyExpirDate";
  String PSWDEXPIRE_SESSION_KEY = "pwdg_passwrod_expire";

  // 60 minutes
  long PSWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES = 60L * 60L * 1000L;
  // 5 minutes
  long PSWDFORGET_MAIL_VERIFY_EXPIRDATE_MILLES = 5L * 60L * 1000L;

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

  // cas登陆Captcha的session值的key
  String CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY = "cas_login_captcha_validation_session_key";

  String PERM_GROUP_OWNER = "PERM_GROUP_OWNER";

  String PERM_USERID_CHECK = "PERM_USERID_CHECK";
  String PERM_ROLEID_CHECK = "PERM_ROLEID_CHECK";
  String PERM_ROLEIDS_CHECK = "PERM_ROLEIDS_CHECK";
  String PERM_PERMID_CHECK = "PERM_PERMID_CHECK";
  String PERM_PERMIDS_CHECK = "PERM_PERMIDS_CHECK";
  String PERM_TAGTYPEID_CHECK = "PERM_TAGTYPEID_CHECK";
  String PERM_TAGID_CHECK = "PERM_TAGID_CHECK";

  String MAIL_PREFIX = "[TechOps]";
  int GLOBALVAR_QUEUE_SIZE = 2048;
  int AUDIT_INSERT_LIST_SIZE = 1024;
  int AUDIT_INSERT_LIST_CACHE_SIZE = 40960;
  int AUDIT_INSERT_EVERY_SECOND = 60;
  int AUDIT_INSERT_EXP_LENGTH = 3072;
  int AUDIT_INSET_PARAM_LENGTH = 256;

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

  String HTTP_METHOD_ALL = "ALL";

  // login ticket prefix
  String CAS_LOGIN_TICKET_PREFIX = "LT";

  // 用户跳转的url路径
  String CAS_CUSTOM_LOGIN_PAGE_KEY = "custom_login_page_key";
  String CAS_CUSTOM_LOGIN_LT_KEY = "custom_login_lt_key";
  String CAS_CUSTOM_LOGIN_URL_KEY = "custom_login_url_key";

  // cxf 传递的locale的key
  @Deprecated
  String CAS_CXF_HEADER_LOCALE_KEY = "cas.cxf.header.locale.key";

  // 默认的租户code
  String DEFAULT_TANANCY_CODE = "DIANRONG";
  // 设置一个非租户相关的租户id
  Long TENANCY_UNRELATED_TENANCY_ID = -1L;
  // 配置项，指定是否强制监测租户信息
  String CHECK_TENANCY_IDENTITY_FORCIBLY = "tenancyIdentity.check.switch";
  // 配置项，指定是否开启uniauth-server的监控开关
  String UNIAUTH_SERVER_API_CALL_SWITCH = "apicall.check.switch";
  // 配置jwt的秘钥
  String UNIAUTH_APICTL_JWT_SECKEY = "apicall.jwt.security.key";
  // 配置techops的api访问密码
  String UNIAUTH_APICTL_TECHOPS_PSWD = "apicall.techops.pwd";

  // 配置项, 指定是否使用notification实现的通知发送
  String UNIAUTH_NOTIFY_USE_NOTIFICATION = "notify.notification";

  // uniauth_server使用的GlobalVarQueue是否采用旧的实现
  String UNIAUTH_GLOBAL_VAR_QUEUE_USE_OLD_IMPL = "global.var.queue.old.impl";

  // 2 hours
  long DEFAULT_API_CALL_TOKEN_AVAILABLE_MILLiSECONDS = 1000L * 60L * 60L * 2L;

  // 记录API访问超时的日志logger name
  String UNIAUTH_API_CALL_TIME_OUT_LOGGER =
      "com.dianrong.common.uniauth.api.call.socket.timeout.exception";
}
