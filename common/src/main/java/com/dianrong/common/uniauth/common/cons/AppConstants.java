package com.dianrong.common.uniauth.common.cons;

/**
 * Created by Arc on 26/1/16.
 */
public interface AppConstants {
    Byte ZERO_Byte = (byte)0;
    Byte ONE_Byte = (byte)1;
    byte ZERO_byte = (byte)0;
    byte ONE_byte = (byte)1;

	String NODE_TYPE_GROUP = "grp";
	String NODE_TYPE_MEMBER_USER = "mUser";
	String NODE_TYPE_OWNER_USER = "oUser";

    String GRP_ROOT = "GRP_ROOT";
    byte MAX_AUTH_FAIL_COUNT = 10;
    int MAX_PASSWORD_VALID_MONTH = 2;
	Integer MAX_PAGE_SIZE = 5000;
	String PERM_TYPE_DOMAIN = "DOMAIN";
	String PERM_TYPE_URIPATTERN = "URI_PATTERN";
	String PERM_TYPE_PRIVILEGE = "PRIVILEGE";
    String DOMAIN_CODE_TECHOPS = "techops";
    
	String ZK_DOMAIN_PREFIX = "domains.";
	String SERVICE_LOGIN_POSTFIX = "/login/cas";
	
	String ROLE_ADMIN = "ROLE_ADMIN";
	String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	
	String AJAS_CROSS_HEADER = "Origin";
	String AJAS_HEADER = "X-Requested-With";
	String LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";
	String NO_PRIVILEGE = "NO_PRIVILEGE";

	String PERM_TYPE_DOMAIN_ID = "DOMAIN_ID";


	//constants for password forget

	//dispatcher parameter
	String PWDFORGET_DISPATCHER_STEP_KEY = "step";
	String PWDFORGET_DISPATCHER_CONTEXTURL_KEY = "savedLoginContext";
	String CAS_CAPTCHA_SESSION_TYPE_KEY = "captchaType";

	//request
	String PWDFORGET_PAGE_VERIFY_CODE_CLIENT_KEY = "pageVerifyCode";
	String PWDFORGET_MAIL_VAL_CLIENT_KEY="email";
	String PWDFORGET_MAIL_VERIFY_CODE_CLIENT_KEY = "verifyCode";
	String PWDFORGET_NEW_PASSWORD_KEY = "newPassword";

	//session
	String CAS_CAPTCHA_SESSION_KEY = "CAS_CAPTCHA_SESSION_KEY";
	String PWDFORGET_MAIL_VAL_KEY = "pwdg_emailVal";
	String PWDFORGET_MAIL_VERIFY_CODE_KEY = "pwdg_verifyCode";
	String PWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY = "pwdg_savedLoginContext";
	String PWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY = "pwdg_verifyExpirDate";

	//60 secondes
	long PWDFORGET_MAIL_VERIFY_CODE_EXPIRE_MILLES = 3L * 60L * 1000L;
	//5 minitues
	long PWDFORGET_MAIL_VERIFY_EXPIRDATE_MILLES = 5L * 60L * 1000L;
	
	//tag for userinfo edit in request
	String CAS_USERINFO_MANAGE_EDIT_KEY = "edituserinfo";
	String CAS_USERINFO_MANAGE_REQUEST_METHOD_KEY = "form_method";
	String CAS_USERINFO_MANAGE_FLOW_REQUEST_METHOD_TYPE_KEY = "user_mangage_flow_request_type";
	String CAS_USERINFO_MANAGE_OPERATE_ERRORMSG_TAG = "user_manage_errormsg";
	
	//更新类型 
	String CAS_USERINFO_MANAGE_UPDATE_METHOD_TYPE_TAG = "updatetype";
	
	//用户信息获取
	String CAS_USERINFO_MANAGE_USER_ID_TAG = "id";
	String CAS_USERINFO_MANAGE_UPDATE_EMAIL_TAG = "email";
	String CAS_USERINFO_MANAGE_UPDATE_PHONE_TAG = "phone";
	String CAS_USERINFO_MANAGE_UPDATE_NAME_TAG = "name";
	String CAS_USERINFO_MANGE_UPDATE_PASSWORD_TAG = "password";
	String CAS_USERINFO_MANGE_UPDATE_ORIGINPASSWORD_TAG = "orign_password";
	
	//cas登陆captcha的session值的key
	String CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY = "cas_login_captcha_validation_session_key";
	
	String PERM_GROUP_OWNER = "PERM_GROUP_OWNER";

	String PERM_ROLEID_CHECK = "PERM_ROLEID_CHECK";
	String PERM_ROLEIDS_CHECK = "PERM_ROLEIDS_CHECK";
	String PERM_PERMID_CHECK = "PERM_PERMID_CHECK";
	String PERM_PERMIDS_CHECK = "PERM_PERMIDS_CHECK";

	String MAIL_PREFIX = "[TechOps]";
	int GLOBALVAR_QUEUE_SIZE = 2048;
	int AUDIT_INSERT_LIST_SIZE = 10;
	int AUDIT_INSERT_EXP_LENGTH = 3072;
	int AUDIT_INSET_PARAM_LENGTH = 256;
}
