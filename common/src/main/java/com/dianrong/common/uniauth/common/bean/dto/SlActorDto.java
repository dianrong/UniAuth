package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * . to c 用户的用户model
 * 
 * @author wanglin
 */
public class SlActorDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1139505595299005632L;

	/**
	 *database column SL$ACTOR.ID
	 */
	private Long id;

	/**
	 *database column SL$ACTOR.NAME
	 */
	private String name;

	/**
	 *database column SL$ACTOR.EMAIL
	 */
	private String email;

	/**
	 *database column SL$ACTOR.SSN
	 */
	private String ssn;

	/**
	 *database column SL$ACTOR.SSN_TYPE
	 */
	private Short ssnType;

	/**
	 *database column SL$ACTOR.SSN_SHORT
	 */
	private String ssnShort;

	/**
	 *database column SL$ACTOR.FAILED_AUTH_CNT
	 */
	private Short failedAuthCnt;

	/**
	 *database column SL$ACTOR.TYPE
	 */
	private SlActorEnum.Type type;

	/**
	 *database column SL$ACTOR.ACC_TYPE
	 */
	private Short accType;

	/**
	 *database column SL$ACTOR.RELATION
	 */
	private Short relation;

	/**
	 *database column SL$ACTOR.SUBTYPE
	 */
	private SlActorEnum.SubType subtype;

	/**
	 *database column SL$ACTOR.OWNER_ID
	 */
	private BigDecimal ownerId;

	/**
	 *database column SL$ACTOR.ADMIN_GRP_ID
	 */
	private BigDecimal adminGrpId;

	/**
	 *database column SL$ACTOR.PROG_ID
	 */
	private BigDecimal progId;

	/**
	 *database column SL$ACTOR.REFERER_ID
	 */
	private BigDecimal refererId;

	/**
	 *database column SL$ACTOR.LAST_LOGIN_D
	 */
	private Date lastLoginD;

	/**
	 *database column SL$ACTOR.CREATE_D
	 */
	private Date createD;

	/**
	 *database column SL$ACTOR.CANCEL_D
	 */
	private Date cancelD;

	/**
	 *database column SL$ACTOR.A_STATUS_D
	 */
	private Date aStatusD;

	/**
	 *database column SL$ACTOR.B_STATUS_D
	 */
	private Date bStatusD;

	/**
	 *database column SL$ACTOR.L_STATUS_D
	 */
	private Date lStatusD;

	/**
	 *database column SL$ACTOR.T_STATUS_D
	 */
	private Date tStatusD;

	/**
	 *database column SL$ACTOR.A_STATUS
	 */
	private SlActorEnum.Status aStatus;

	/**
	 *database column SL$ACTOR.B_STATUS
	 */
	private SlActorEnum.Status bStatus;

	/**
	 *database column SL$ACTOR.L_STATUS
	 */
	private SlActorEnum.Status lStatus;

	/**
	 *database column SL$ACTOR.T_STATUS
	 */
	private SlActorEnum.Status tStatus;

	/**
	 *database column SL$ACTOR.U_STATUS
	 */
	private SlActorEnum.Status uStatus;

	/**
	 *database column SL$ACTOR.FNAME
	 */
	private String fname;

	/**
	 *database column SL$ACTOR.LNAME
	 */
	private String lname;

	/**
	 *database column SL$ACTOR.MNAME
	 */
	private String mname;

	/**
	 *database column SL$ACTOR.BALANCE
	 */
	private BigDecimal balance;

	/**
	 *database column SL$ACTOR.LOCKED_FOR_INV
	 */
	private BigDecimal lockedForInv;

	/**
	 *database column SL$ACTOR.ADVANCE_AMT
	 */
	private BigDecimal advanceAmt;

	/**
	 *database column SL$ACTOR.ADVANCE_OVERRIDE
	 */
	private BigDecimal advanceOverride;

	/**
	 *database column SL$ACTOR.AUTO_ACH_QP_END_D
	 */
	private Date autoAchQpEndD;

	/**
	 *database column SL$ACTOR.DIST_FREEZE_END_D
	 */
	private Date distFreezeEndD;

	/**
	 *database column SL$ACTOR.WAVE_FEE_UNTIL_D
	 */
	private Date waveFeeUntilD;

	/**
	 *database column SL$ACTOR.WAVE_LCFEE_UNTIL_D
	 */
	private Date waveLcfeeUntilD;

	/**
	 *database column SL$ACTOR.LENDING_STATE
	 */
	private String lendingState;

	/**
	 *database column SL$ACTOR.AGREE_COMPLIANT
	 */
	private Short agreeCompliant;

	/**
	 *database column SL$ACTOR.WAROIC
	 */
	private BigDecimal waroic;

	/**
	 *database column SL$ACTOR.FLAGS
	 */
	private BigDecimal flags;

	/**
	 *database column SL$ACTOR.YTM
	 */
	private Short ytm;

	/**
	 *database column SL$ACTOR.DEVICE_TOKEN_IDS
	 */
	private String deviceTokenIds;

	/**
	 *database column
	 * SL$ACTOR.MOBILE_INSTALLATION_D
	 */
	private Date mobileInstallationD;

	/**
	 *database column SL$ACTOR.EMAIL_OPTOUT
	 */
	private Short emailOptout;

	/**
	 *database column SL$ACTOR.COUNTRY_CODE
	 */
	private String countryCode;

	/**
	 *database column SL$ACTOR.VIP_LEVEL
	 */
	private Short vipLevel;

	/**
	 *database column SL$ACTOR.PARTNER_ID
	 */
	private BigDecimal partnerId;

	/**
	 *database column SL$ACTOR.APP_CONFIG_ID
	 */
	private BigDecimal appConfigId;

	/**
	 *database column SL$ACTOR.CELLPHONE
	 */
	private String cellphone;

	/**
	 *database column SL$ACTOR.PROFILE_IMAGE_ADDR
	 */
	private String profileImageAddr;

	/**
	 *database column SL$ACTOR.EMPLOYEE_ID
	 */
	private BigDecimal employeeId;

	/**
	 *database column SL$ACTOR.LAST_RESETPWD_D
	 */
	private Date lastResetpwdD;

	/**
	 *database column SL$ACTOR.AUTO_REINVEST
	 */
	private Short autoReinvest;

	/**
	 *database column SL$ACTOR.PARENT_ID
	 */
	private BigDecimal parentId;

	/**
	 *database column
	 * SL$ACTOR.REGISTER_CHANNEL_ID
	 */
	private BigDecimal registerChannelId;

	/**
	 *database column SL$ACTOR.MARKET_CHANNEL
	 */
	private String marketChannel;

	/**
	 *database column
	 * SL$ACTOR.BANK_CARD_BINDING_STATUS
	 */
	private Short bankCardBindingStatus;

	/**
	 *database column SL$ACTOR.ACC_OPEN_STATUS
	 */
	private Short accOpenStatus;

	/**
	 *database column
	 * SL$ACTOR.CARD_VERIFIED_STATUS
	 */
	private Short cardVerifiedStatus;

	/**
	 *database column
	 * SL$ACTOR.BALANCE_SYNC_STATUS
	 */
	private Short balanceSyncStatus;

	/**
	 *database column SL$ACTOR.B_DAY
	 */
	private String bDay;
	
	  /**
     * column SL$ACTOR.CREDENTIAL
     */
    private byte[] credential;

    /**
     * column SL$ACTOR.PAYMENT_PASSWORD
     */
    private byte[] paymentPassword;

    /**
     * column SL$ACTOR.CREDENTIAL_SALT
     */
    private byte[] credentialSalt;

    /**
     * column SL$ACTOR.PAYMENT_PASSWORD_SALT
     */
    private byte[] paymentPasswordSalt;

	/**
	 * @return the credential
	 */
	public byte[] getCredential() {
		return credential;
	}

	/**
	 * @param credential the credential to set
	 */
	public SlActorDto setCredential(byte[] credential) {
		this.credential = credential;
		return this;
	}

	/**
	 * @return the paymentPassword
	 */
	public byte[] getPaymentPassword() {
		return paymentPassword;
	}

	/**
	 * @param paymentPassword the paymentPassword to set
	 */
	public SlActorDto setPaymentPassword(byte[] paymentPassword) {
		this.paymentPassword = paymentPassword;
		return this;
	}

	/**
	 * @return the credentialSalt
	 */
	public byte[] getCredentialSalt() {
		return credentialSalt;
	}

	/**
	 * @param credentialSalt the credentialSalt to set
	 */
	public SlActorDto setCredentialSalt(byte[] credentialSalt) {
		this.credentialSalt = credentialSalt;
		return this;
	}

	/**
	 * @return the paymentPasswordSalt
	 */
	public byte[] getPaymentPasswordSalt() {
		return paymentPasswordSalt;
	}

	/**
	 * @param paymentPasswordSalt the paymentPasswordSalt to set
	 */
	public SlActorDto setPaymentPasswordSalt(byte[] paymentPasswordSalt) {
		this.paymentPasswordSalt = paymentPasswordSalt;
		return this;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public SlActorDto setId(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public SlActorDto setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public SlActorDto setEmail(String email) {
		this.email = email;
		return this;
	}

	/**
	 * @return the ssn
	 */
	public String getSsn() {
		return ssn;
	}

	/**
	 * @param ssn the ssn to set
	 */
	public SlActorDto setSsn(String ssn) {
		this.ssn = ssn;
		return this;
	}

	/**
	 * @return the ssnType
	 */
	public Short getSsnType() {
		return ssnType;
	}

	/**
	 * @param ssnType the ssnType to set
	 */
	public SlActorDto setSsnType(Short ssnType) {
		this.ssnType = ssnType;
		return this;
	}

	/**
	 * @return the ssnShort
	 */
	public String getSsnShort() {
		return ssnShort;
	}

	/**
	 * @param ssnShort the ssnShort to set
	 */
	public SlActorDto setSsnShort(String ssnShort) {
		this.ssnShort = ssnShort;
		return this;
	}

	/**
	 * @return the failedAuthCnt
	 */
	public Short getFailedAuthCnt() {
		return failedAuthCnt;
	}

	/**
	 * @param failedAuthCnt the failedAuthCnt to set
	 */
	public SlActorDto setFailedAuthCnt(Short failedAuthCnt) {
		this.failedAuthCnt = failedAuthCnt;
		return this;
	}

	/**
	 * @return the type
	 */
	public SlActorEnum.Type getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public SlActorDto setType(SlActorEnum.Type type) {
		this.type = type;
		return this;
	}

	/**
	 * @return the accType
	 */
	public Short getAccType() {
		return accType;
	}

	/**
	 * @param accType the accType to set
	 */
	public SlActorDto setAccType(Short accType) {
		this.accType = accType;
		return this;
	}

	/**
	 * @return the relation
	 */
	public Short getRelation() {
		return relation;
	}

	/**
	 * @param relation the relation to set
	 */
	public SlActorDto setRelation(Short relation) {
		this.relation = relation;
		return this;
	}

	/**
	 * @return the subtype
	 */
	public SlActorEnum.SubType getSubtype() {
		return subtype;
	}

	/**
	 * @param subtype the subtype to set
	 */
	public SlActorDto setSubtype(SlActorEnum.SubType subtype) {
		this.subtype = subtype;
		return this;
	}

	/**
	 * @return the ownerId
	 */
	public BigDecimal getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public SlActorDto setOwnerId(BigDecimal ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	/**
	 * @return the adminGrpId
	 */
	public BigDecimal getAdminGrpId() {
		return adminGrpId;
	}

	/**
	 * @param adminGrpId the adminGrpId to set
	 */
	public SlActorDto setAdminGrpId(BigDecimal adminGrpId) {
		this.adminGrpId = adminGrpId;
		return this;
	}

	/**
	 * @return the progId
	 */
	public BigDecimal getProgId() {
		return progId;
	}

	/**
	 * @param progId the progId to set
	 */
	public SlActorDto setProgId(BigDecimal progId) {
		this.progId = progId;
		return this;
	}

	/**
	 * @return the refererId
	 */
	public BigDecimal getRefererId() {
		return refererId;
	}

	/**
	 * @param refererId the refererId to set
	 */
	public SlActorDto setRefererId(BigDecimal refererId) {
		this.refererId = refererId;
		return this;
	}

	/**
	 * @return the lastLoginD
	 */
	public Date getLastLoginD() {
		return lastLoginD;
	}

	/**
	 * @param lastLoginD the lastLoginD to set
	 */
	public SlActorDto setLastLoginD(Date lastLoginD) {
		this.lastLoginD = lastLoginD;
		return this;
	}

	/**
	 * @return the createD
	 */
	public Date getCreateD() {
		return createD;
	}

	/**
	 * @param createD the createD to set
	 */
	public SlActorDto setCreateD(Date createD) {
		this.createD = createD;
		return this;
	}

	/**
	 * @return the cancelD
	 */
	public Date getCancelD() {
		return cancelD;
	}

	/**
	 * @param cancelD the cancelD to set
	 */
	public SlActorDto setCancelD(Date cancelD) {
		this.cancelD = cancelD;
		return this;
	}

	/**
	 * @return the aStatusD
	 */
	public Date getaStatusD() {
		return aStatusD;
	}

	/**
	 * @param aStatusD the aStatusD to set
	 */
	public SlActorDto setaStatusD(Date aStatusD) {
		this.aStatusD = aStatusD;
		return this;
	}

	/**
	 * @return the bStatusD
	 */
	public Date getbStatusD() {
		return bStatusD;
	}

	/**
	 * @param bStatusD the bStatusD to set
	 */
	public SlActorDto setbStatusD(Date bStatusD) {
		this.bStatusD = bStatusD;
		return this;
	}

	/**
	 * @return the lStatusD
	 */
	public Date getlStatusD() {
		return lStatusD;
	}

	/**
	 * @param lStatusD the lStatusD to set
	 */
	public SlActorDto setlStatusD(Date lStatusD) {
		this.lStatusD = lStatusD;
		return this;
	}

	/**
	 * @return the tStatusD
	 */
	public Date gettStatusD() {
		return tStatusD;
	}

	/**
	 * @param tStatusD the tStatusD to set
	 */
	public SlActorDto settStatusD(Date tStatusD) {
		this.tStatusD = tStatusD;
		return this;
	}

	/**
	 * @return the aStatus
	 */
	public SlActorEnum.Status getaStatus() {
		return aStatus;
	}

	/**
	 * @param aStatus the aStatus to set
	 */
	public SlActorDto setaStatus(SlActorEnum.Status aStatus) {
		this.aStatus = aStatus;
		return this;
	}

	/**
	 * @return the bStatus
	 */
	public SlActorEnum.Status getbStatus() {
		return bStatus;
	}

	/**
	 * @param bStatus the bStatus to set
	 */
	public SlActorDto setbStatus(SlActorEnum.Status bStatus) {
		this.bStatus = bStatus;
		return this;
	}

	/**
	 * @return the lStatus
	 */
	public SlActorEnum.Status getlStatus() {
		return lStatus;
	}

	/**
	 * @param lStatus the lStatus to set
	 */
	public SlActorDto setlStatus(SlActorEnum.Status lStatus) {
		this.lStatus = lStatus;
		return this;
	}

	/**
	 * @return the tStatus
	 */
	public SlActorEnum.Status gettStatus() {
		return tStatus;
	}

	/**
	 * @param tStatus the tStatus to set
	 */
	public SlActorDto settStatus(SlActorEnum.Status tStatus) {
		this.tStatus = tStatus;
		return this;
	}

	/**
	 * @return the uStatus
	 */
	public SlActorEnum.Status getuStatus() {
		return uStatus;
	}

	/**
	 * @param uStatus the uStatus to set
	 */
	public SlActorDto setuStatus(SlActorEnum.Status uStatus) {
		this.uStatus = uStatus;
		return this;
	}

	/**
	 * @return the fname
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * @param fname the fname to set
	 */
	public SlActorDto setFname(String fname) {
		this.fname = fname;
		return this;
	}

	/**
	 * @return the lname
	 */
	public String getLname() {
		return lname;
	}

	/**
	 * @param lname the lname to set
	 */
	public SlActorDto setLname(String lname) {
		this.lname = lname;
		return this;
	}

	/**
	 * @return the mname
	 */
	public String getMname() {
		return mname;
	}

	/**
	 * @param mname the mname to set
	 */
	public SlActorDto setMname(String mname) {
		this.mname = mname;
		return this;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public SlActorDto setBalance(BigDecimal balance) {
		this.balance = balance;
		return this;
	}

	/**
	 * @return the lockedForInv
	 */
	public BigDecimal getLockedForInv() {
		return lockedForInv;
	}

	/**
	 * @param lockedForInv the lockedForInv to set
	 */
	public SlActorDto setLockedForInv(BigDecimal lockedForInv) {
		this.lockedForInv = lockedForInv;
		return this;
	}

	/**
	 * @return the advanceAmt
	 */
	public BigDecimal getAdvanceAmt() {
		return advanceAmt;
	}

	/**
	 * @param advanceAmt the advanceAmt to set
	 */
	public SlActorDto setAdvanceAmt(BigDecimal advanceAmt) {
		this.advanceAmt = advanceAmt;
		return this;
	}

	/**
	 * @return the advanceOverride
	 */
	public BigDecimal getAdvanceOverride() {
		return advanceOverride;
	}

	/**
	 * @param advanceOverride the advanceOverride to set
	 */
	public SlActorDto setAdvanceOverride(BigDecimal advanceOverride) {
		this.advanceOverride = advanceOverride;
		return this;
	}

	/**
	 * @return the autoAchQpEndD
	 */
	public Date getAutoAchQpEndD() {
		return autoAchQpEndD;
	}

	/**
	 * @param autoAchQpEndD the autoAchQpEndD to set
	 */
	public SlActorDto setAutoAchQpEndD(Date autoAchQpEndD) {
		this.autoAchQpEndD = autoAchQpEndD;
		return this;
	}

	/**
	 * @return the distFreezeEndD
	 */
	public Date getDistFreezeEndD() {
		return distFreezeEndD;
	}

	/**
	 * @param distFreezeEndD the distFreezeEndD to set
	 */
	public SlActorDto setDistFreezeEndD(Date distFreezeEndD) {
		this.distFreezeEndD = distFreezeEndD;
		return this;
	}

	/**
	 * @return the waveFeeUntilD
	 */
	public Date getWaveFeeUntilD() {
		return waveFeeUntilD;
	}

	/**
	 * @param waveFeeUntilD the waveFeeUntilD to set
	 */
	public SlActorDto setWaveFeeUntilD(Date waveFeeUntilD) {
		this.waveFeeUntilD = waveFeeUntilD;
		return this;
	}

	/**
	 * @return the waveLcfeeUntilD
	 */
	public Date getWaveLcfeeUntilD() {
		return waveLcfeeUntilD;
	}

	/**
	 * @param waveLcfeeUntilD the waveLcfeeUntilD to set
	 */
	public SlActorDto setWaveLcfeeUntilD(Date waveLcfeeUntilD) {
		this.waveLcfeeUntilD = waveLcfeeUntilD;
		return this;
	}

	/**
	 * @return the lendingState
	 */
	public String getLendingState() {
		return lendingState;
	}

	/**
	 * @param lendingState the lendingState to set
	 */
	public SlActorDto setLendingState(String lendingState) {
		this.lendingState = lendingState;
		return this;
	}

	/**
	 * @return the agreeCompliant
	 */
	public Short getAgreeCompliant() {
		return agreeCompliant;
	}

	/**
	 * @param agreeCompliant the agreeCompliant to set
	 */
	public SlActorDto setAgreeCompliant(Short agreeCompliant) {
		this.agreeCompliant = agreeCompliant;
		return this;
	}

	/**
	 * @return the waroic
	 */
	public BigDecimal getWaroic() {
		return waroic;
	}

	/**
	 * @param waroic the waroic to set
	 */
	public SlActorDto setWaroic(BigDecimal waroic) {
		this.waroic = waroic;
		return this;
	}

	/**
	 * @return the flags
	 */
	public BigDecimal getFlags() {
		return flags;
	}

	/**
	 * @param flags the flags to set
	 */
	public SlActorDto setFlags(BigDecimal flags) {
		this.flags = flags;
		return this;
	}

	/**
	 * @return the ytm
	 */
	public Short getYtm() {
		return ytm;
	}

	/**
	 * @param ytm the ytm to set
	 */
	public SlActorDto setYtm(Short ytm) {
		this.ytm = ytm;
		return this;
	}

	/**
	 * @return the deviceTokenIds
	 */
	public String getDeviceTokenIds() {
		return deviceTokenIds;
	}

	/**
	 * @param deviceTokenIds the deviceTokenIds to set
	 */
	public SlActorDto setDeviceTokenIds(String deviceTokenIds) {
		this.deviceTokenIds = deviceTokenIds;
		return this;
	}

	/**
	 * @return the mobileInstallationD
	 */
	public Date getMobileInstallationD() {
		return mobileInstallationD;
	}

	/**
	 * @param mobileInstallationD the mobileInstallationD to set
	 */
	public SlActorDto setMobileInstallationD(Date mobileInstallationD) {
		this.mobileInstallationD = mobileInstallationD;
		return this;
	}

	/**
	 * @return the emailOptout
	 */
	public Short getEmailOptout() {
		return emailOptout;
	}

	/**
	 * @param emailOptout the emailOptout to set
	 */
	public SlActorDto setEmailOptout(Short emailOptout) {
		this.emailOptout = emailOptout;
		return this;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public SlActorDto setCountryCode(String countryCode) {
		this.countryCode = countryCode;
		return this;
	}

	/**
	 * @return the vipLevel
	 */
	public Short getVipLevel() {
		return vipLevel;
	}

	/**
	 * @param vipLevel the vipLevel to set
	 */
	public SlActorDto setVipLevel(Short vipLevel) {
		this.vipLevel = vipLevel;
		return this;
	}

	/**
	 * @return the partnerId
	 */
	public BigDecimal getPartnerId() {
		return partnerId;
	}

	/**
	 * @param partnerId the partnerId to set
	 */
	public SlActorDto setPartnerId(BigDecimal partnerId) {
		this.partnerId = partnerId;
		return this;
	}

	/**
	 * @return the appConfigId
	 */
	public BigDecimal getAppConfigId() {
		return appConfigId;
	}

	/**
	 * @param appConfigId the appConfigId to set
	 */
	public SlActorDto setAppConfigId(BigDecimal appConfigId) {
		this.appConfigId = appConfigId;
		return this;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}

	/**
	 * @param cellphone the cellphone to set
	 */
	public SlActorDto setCellphone(String cellphone) {
		this.cellphone = cellphone;
		return this;
	}

	/**
	 * @return the profileImageAddr
	 */
	public String getProfileImageAddr() {
		return profileImageAddr;
	}

	/**
	 * @param profileImageAddr the profileImageAddr to set
	 */
	public SlActorDto setProfileImageAddr(String profileImageAddr) {
		this.profileImageAddr = profileImageAddr;
		return this;
	}

	/**
	 * @return the employeeId
	 */
	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public SlActorDto setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
		return this;
	}

	/**
	 * @return the lastResetpwdD
	 */
	public Date getLastResetpwdD() {
		return lastResetpwdD;
	}

	/**
	 * @param lastResetpwdD the lastResetpwdD to set
	 */
	public SlActorDto setLastResetpwdD(Date lastResetpwdD) {
		this.lastResetpwdD = lastResetpwdD;
		return this;
	}

	/**
	 * @return the autoReinvest
	 */
	public Short getAutoReinvest() {
		return autoReinvest;
	}

	/**
	 * @param autoReinvest the autoReinvest to set
	 */
	public SlActorDto setAutoReinvest(Short autoReinvest) {
		this.autoReinvest = autoReinvest;
		return this;
	}

	/**
	 * @return the parentId
	 */
	public BigDecimal getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public SlActorDto setParentId(BigDecimal parentId) {
		this.parentId = parentId;
		return this;
	}

	/**
	 * @return the registerChannelId
	 */
	public BigDecimal getRegisterChannelId() {
		return registerChannelId;
	}

	/**
	 * @param registerChannelId the registerChannelId to set
	 */
	public SlActorDto setRegisterChannelId(BigDecimal registerChannelId) {
		this.registerChannelId = registerChannelId;
		return this;
	}

	/**
	 * @return the marketChannel
	 */
	public String getMarketChannel() {
		return marketChannel;
	}

	/**
	 * @param marketChannel the marketChannel to set
	 */
	public SlActorDto setMarketChannel(String marketChannel) {
		this.marketChannel = marketChannel;
		return this;
	}

	/**
	 * @return the bankCardBindingStatus
	 */
	public Short getBankCardBindingStatus() {
		return bankCardBindingStatus;
	}

	/**
	 * @param bankCardBindingStatus the bankCardBindingStatus to set
	 */
	public SlActorDto setBankCardBindingStatus(Short bankCardBindingStatus) {
		this.bankCardBindingStatus = bankCardBindingStatus;
		return this;
	}

	/**
	 * @return the accOpenStatus
	 */
	public Short getAccOpenStatus() {
		return accOpenStatus;
	}

	/**
	 * @param accOpenStatus the accOpenStatus to set
	 */
	public SlActorDto setAccOpenStatus(Short accOpenStatus) {
		this.accOpenStatus = accOpenStatus;
		return this;
	}

	/**
	 * @return the cardVerifiedStatus
	 */
	public Short getCardVerifiedStatus() {
		return cardVerifiedStatus;
	}

	/**
	 * @param cardVerifiedStatus the cardVerifiedStatus to set
	 */
	public SlActorDto setCardVerifiedStatus(Short cardVerifiedStatus) {
		this.cardVerifiedStatus = cardVerifiedStatus;
		return this;
	}

	/**
	 * @return the balanceSyncStatus
	 */
	public Short getBalanceSyncStatus() {
		return balanceSyncStatus;
	}

	/**
	 * @param balanceSyncStatus the balanceSyncStatus to set
	 */
	public SlActorDto setBalanceSyncStatus(Short balanceSyncStatus) {
		this.balanceSyncStatus = balanceSyncStatus;
		return this;
	}

	/**
	 * @return the bDay
	 */
	public String getbDay() {
		return bDay;
	}

	/**
	 * @param bDay the bDay to set
	 */
	public SlActorDto setbDay(String bDay) {
		this.bDay = bDay;
		return this;
	}
	
	// 业务bean提供的一些工具方法
	
	// 判断电话号码是否合法
	 public boolean isCellphoneVerified() {
		    return this.isFlagSet(SlActorEnum.Flag.CELLPHONE_VERIFIED)
		        && !StringUtil.strIsNullOrEmpty(this.cellphone);
		  }
	 
	 /**.
	  * 判断标志位的
	  * @param f
	  * @return
	  */
	 public boolean isFlagSet(SlActorEnum.Flag f) {
		    return (this.flags.longValue() & (1l << f.ordinal())) != 0;
	 }
	 
	 /**.
	  * 获取用户的类型信息
	  * @return
	  */
	 public SlActorEnum.LCUserIntent getUserIntent() {
		    if (isRole(SlActorEnum.LCRole.BORROWER, SlActorEnum.Status.UNCONFIRMED, SlActorEnum.Status.CONFIRMED)) {
		      return SlActorEnum.LCUserIntent.BORROWER;
		    } else if (isRole(SlActorEnum.LCRole.LENDER, SlActorEnum.Status.UNCONFIRMED, SlActorEnum.Status.CONFIRMED)) {
		      return SlActorEnum.LCUserIntent.LENDER;
		    }
		    return SlActorEnum.LCUserIntent.NONE;
		  }

		  public boolean isRole(SlActorEnum.LCRole role, SlActorEnum.Status... status) {
			  SlActorEnum.Status stat = null;
		    if (role == null)
		      return false;

		    switch (role) {
		    case LENDER:
		      stat = this.getlStatus();
		      break;
		    case BORROWER:
		      stat = this.getbStatus();
		      break;
		    case ENDUSER:
		      stat = this.getuStatus();
		      break;
		    case AGENT:
		      stat = this.getaStatus();
		      break;
		    case TRADER:
		      stat = this.gettStatus();
		      break;
		    default:
		      break;
		    }

		    if (stat == null)
		      return false;

		    if (status != null && stat.in(status))
		      return true;

		    // if status == null or is empty then we are checking that A a status is set
		    if (status == null || status.length == 0) {
		      switch (stat) {
		      case ANY:
		      case NONE:
		        return false;
		      default:
		        return true;
		      }
		    }
		    return false;
		  }
}
