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

  // 组织关系的根组Code
  String ORGANIZATION_ROOT = "ORGANIZATION_ROOT";
  String GRP_ROOT = "GRP_ROOT";
  String TECHOPS_SUPER_ADMIN_GRP = "GRP_TECHOPS_SUPER_ADMIN";

  byte MAX_AUTH_FAIL_COUNT = 10;
  int MAX_PASSWORD_VALID_MONTH = 12;
  int DUPLICATE_PWD_VALID_MONTH = 12;
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

  // 60 minutes
  long PSWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES = 60L * 60L * 1000L;
  // 5 minutes
  long PSWDFORGET_MAIL_VERIFY_EXPIRDATE_MILLES = 5L * 60L * 1000L;

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

  String HTTP_METHOD_ALL = "ALL";

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
