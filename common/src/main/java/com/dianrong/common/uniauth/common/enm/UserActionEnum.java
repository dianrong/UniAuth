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
	UPDATE_PASSWORD_BY_ACCOUNT,
	;
    
    /**
     * check the action is update info by account
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
}
