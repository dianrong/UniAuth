package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;

/**.
 * 用户相关枚举
 * @author wanglin
 */
public class SlActorEnum implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -636628963249599839L;

	/**.
	 * login type
	 * @author wanglin
	 */
	public enum LoginWith {
	    USERNAME,
	    EMAIL,
	    CELLPHONE,
	  }
	
	/**.
	 * flag
	 * @author wanglin
	 *
	 */
	 public static enum Flag {
		    EMAIL_VERIFIED, // 0 -
		    TRUSTED_USER, // 1 users that have been trusted and can withraw funds
		                  // without verification
		    PRIME_USER, // 2- prime users
		    InvitationPartner, // 3- SL invitation partner
		    SMA, // 4 - Separately managed accounts
		    ForeignLender, // 5
		    Impersonator, // 6
		    CELLPHONE_VERIFIED, // 7
		    PAY_LOAN, // 8 Actor can help other borrower pay the loan
		    VIRTUAL_USER, // 9 virtual USER
		    QA_BLOCK, // 10 Actor been blocked post QA
		    QA_UNLIMITED, // 11 Actor can post QA without limit
		    BN_FULLY_VIEW, // 12 privilige to view full list in browse notes
		    FUNNEL_USER,

		    ;

		    private Flag() {
		      if (this.ordinal() >= Long.SIZE)
		        throw new IndexOutOfBoundsException("Flag ordinal is too big");
		    }

		    public long val() {
		      return (long) Math.pow(2, ordinal());
		    }
		  }
	
	/**.
	 * actor type enum
	 * @author wanglin
	 */
	public static enum Type {
	    GROUP, // 0 - group
	    ANY, // 1 - Any
	    UNCRUNCH_USER, // 2 - uncrunch user
	    DO_NOT_USE, // 3 -
	    OPERATOR, // 4 - Account to get a session in the main App, but only have
	              // access to a select set of operations (not lender/borrower
	              // pages)
	    LC_SYS_ACTOR, // 5 - An ACTOR representing LC (example W1, fraud wallet,
	                  // webbank,etc.)
	    ADMIN, // 6 - admin user
	    USER, // 7 - regular user
	    ;
	    private final static Type[] myEnumValues = Type.values();

	    public static Type fromShort(Short x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }
	    
	    public static Type fromInteger(Integer x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }

	    public boolean in(SlActorEnum.Type[] types) {
	      for (SlActorEnum.Type t : types) {
	        if (t == this)
	          return true;
	      }
	      return false;
	    }
	  }
	
	/**.
	 * status
	 * @author wanglin
	 *
	 */
	public static enum Status {
	    ANY, // 0 - internal state to be used by admin only
	    NONE, // 1 - no status available
	    PENDING, // 2 - pending
	    CONFIRMED, // 3 - confirmed
	    UNCONFIRMED, // 4 - unconfirmed
	    CANCELED, // 5 - marked Removed so it won't show in the query results..
	    INVITED, // 6 - invited user.
	    CANCEL_PENDING, // 7 - cancelation pending
	    FRAUD, // 8 - account is fradulant
	    REJECTED, // 9 - application rejected
	    BK_FILED, // 10 - bankrupcy filed
	    ;

	    private final static Status[] myEnumValues = Status.values();

	    public static Status fromShort(Short x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }
	    
	    public static Status fromInteger(Integer x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }

	    public boolean in(Status... s) {
	      for (Status stat : s) {
	        if (this == stat)
	          return true;
	      }
	      return false;
	    }
	  }

	// subType
	public static enum SubType {
	    ANY, // 0 - internal -- for admin app use only
	    CHARITY, // 1
	    REFERAL_PARTNER, // 2
	    ENTRUST_IRA, // 3
	    GroupRole, // 4
	    PRIVATE_PLACEMENT, // 5
	    INSTITUTIONS, // 6 SVB, GH, NVP ,企业注册Actor
	    SUPER_USER, // 7 super user
	    SR_RECEIVER, // 8 SR Receiver - DO NOT USE. THIS HAS BEEN REPLACED WITH THE
	                 // SR REVIEWER GROUP.
	    PRIME, // 9 Prime investor
	    INV_CLIENTS, // 10 group of clients who have advisors assigned to them
	    WORKFLOW, // 11 work flow user
	    SDIRA_IRA, // 12 New SDIRA partner with company SDIRA
	    COSIGNER, // 13 indicates the user is a cosigner
	    REFERAL_PARTNER_AGENT, // 14 XML partner agent
	    XML_REFERAL_PARTNER, // 15 XML partner
	    LINK_REFERAL_PARTNER, // 16 XML partner
	    FOREIGNER, // 17 外国人
	    BORROWER_SALES_AGENT, // 18 Agent in Borrower sales
	    LENDER_SALES_AGENT, // 19 Agent in Lender sales
	    AGENT_CANDIDATE, // 20 Candidate to become an agent after meeting certain
	                     // criteria
	    IMPERSONATE_LIMIT, // 21 For admin users that who don't have full
	                       // impersonate privilege
	                       // virtual accounts
	    VIRTUAL, // 22 for virtual lender or borrowers
	    PROMOTION_INITIATOR,  // 23 for marketing promotion use
	    INVEST_CHANNEL_CUSTODIAN, // 24 for invest channel withdraw
	    SUB_ACCOUNT; // 25 to support the main-sub account concept for 1 physical person
	    
	    private final static SubType[] myEnumValues = SubType.values();

	    public static SubType fromShort(Short x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }
	    
	    public static SubType fromInteger(Integer x) {
	    	if(x == null) {
	    		return null;
	    	}
	      return myEnumValues[x];
	    }

	    public static boolean isReferral(SubType s) {
	      if (s != null
	          && (s == REFERAL_PARTNER || s == XML_REFERAL_PARTNER
	              || s == LINK_REFERAL_PARTNER || s == REFERAL_PARTNER_AGENT
	              || s == BORROWER_SALES_AGENT || s == LENDER_SALES_AGENT)) {
	        return true;
	      }
	      return false;
	    }

	    public static SubType getSubTypeByName(String name) {
	      for (SubType item : SubType.values()) {
	        if (item.name().equals(name)) {
	          return item;
	        }
	      }
	      return null;
	    }// end
	  }

	  public static enum AuthStatus {
	    PASS, // 0 - authentication succeeded
	    FAIL_CRED, // 1 - auth. failed because of credential missmatch
	    FAIL_FAAC, // 2 - auth. failed because FAAC threshhold has been exceeded
	    FAIL_LOCK // 3 - auth. failed because account is locked (ie. FAAC is very
	              // high)
	  }

	  public enum LCUserIntent {
	    NONE, // 0
	    LENDER, // 1 - Intent for users registered as lenders
	    BORROWER, // 2 - Intent for users registered as borrowers
	    SUBSCRIPTOR, // 3 - Intent for users suscribed to receive updates
	  }

	  public enum LCRole {
	    LENDER, BORROWER, ENDUSER, AGENT, TRADER, CANCELED, INVITED, LC_SYS_ACTOR, ADMIN, CHARITY, UNCONF_USER, REFERAL_PARTNER, ANY, FRAUD, BORROWER_SALES_AGENT, LENDER_SALES_AGENT, AGENT_CANDIDATE, ;
	    public boolean in(LCRole... ooo) {
	      for (LCRole o : ooo)
	        if (this == o)
	          return true;
	      return false;
	    }
	  }
}
