package com.dianrong.common.uniauth.common.bean.dto;

/**.
 * 定义slip相关的enum
 * @author wanglin
 *
 */
public class SlIpEnum {
	// ============================================================================
	  // ============================================================================
	  // ============================================================================
	  // all non top link code and fields should go below this line

	  // --------------------------------------------------------------------------
	  public enum Status {
	    ANY, // 0 - internal for admin app use
	    NORMAL, // 1
	    PINNED, // 2
	    PINNED_10, // 3
	    NOT_FOUND, // 4
	    ERROR, // 5
	    GC, // 6 -- marked for garbage collection
	    ;
	  }

	  // --------------------------------------------------------------------------
	  public enum Type {
	    ANY, // 0 - internal -- for admin app use
	    LOGIN, // 1 -
	    LOAN_APP, // 2 -
	    FUNDING_SRC, // 3 - funding source creation/change
	    PASSWD_CHG, // 4 - password change
	    PASSWD_RESET, // 5 - password reset
	    PROFILE_CHG, // 6 - email, profile change
	    NAME_CHG, // 7 - name change
	    ACTIVATE_LOAN_OFFER, // 8 - activate loan offer
	    WEB_SERVICE, // 9 - web service
	    CREATE_ACCOUNT, // 10 - create account
	    ;
	  }

	  // --------------------------------------------------------------------------
	  private enum Flag {
	    ;
	    private Flag() {
	      if (this.ordinal() >= Long.SIZE)
	        throw new IndexOutOfBoundsException("enum size is too big");
	    }
	  }
}
