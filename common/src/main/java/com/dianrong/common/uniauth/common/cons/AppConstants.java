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
    String DOMAIN_CODE_TECHOPS = "techops";
    
	String ZK_DOMAIN_PREFIX = "domains.";
	String SERVICE_LOGIN_POSTFIX = "/login/cas";
	String CAS_CAPTCHA_SESSION_KEY = "CAS_CAPTCHA_SESSION_KEY";
	
	String ROLE_ADMIN = "ROLE_ADMIN";
	String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	
	String AJAS_CROSS_HEADER = "Origin";
	String AJAS_HEADER = "X-Requested-With";
	String LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";
	String NO_PRIVILEGE = "NO_PRIVILEGE";

	String PERM_TYPE_DOMAIN_ID = "DOMAIN_ID";

	String PERM_GROUP_OWNER = "PERM_GROUP_OWNER";

	String PERM_ROLEID_CHECK = "PERM_ROLEID_CHECK";
	String PERM_ROLEIDS_CHECK = "PERM_ROLEIDS_CHECK";

	String MAIL_PREFIX = "[TechOps]";
	int GLOBALVAR_QUEUE_SIZE = 2048;
	int AUDIT_INSERT_LIST_SIZE = 10;
	int AUDIT_INSERT_EXP_LENGTH = 3072;
	int AUDIT_INSET_PARAM_LENGTH = 256;
}
