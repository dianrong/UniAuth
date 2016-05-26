package com.dianrong.common.uniauth.common.bean.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SlThirdLoginDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4652429608170665214L;

	/**.
	 * 泛型  用于处理loginType
	 * @author wanglin
	 */
	public enum SourceType implements Serializable {
	    WEIBO, QQ, WECHAT, DIANRONG
	  }
	
	 /**
     * column SL$THIRD_LOGIN.OPEN_UID
     */
    private String openUid;

    /**
     * column SL$THIRD_LOGIN.LOGIN_TYPE
     */
    private Short loginType;
	
    /**
     * column SL$THIRD_LOGIN.BIND_AID
     */
    private BigDecimal bindAid;

    /**
     * column SL$THIRD_LOGIN.NICK_NAME
     */
    private String nickName;

    /**
     * column SL$THIRD_LOGIN.ACCESS_TOKEN
     */
    private String accessToken;

    /**
     * column SL$THIRD_LOGIN.EXPIRES_IN
     */
    private String expiresIn;

	/**
	 * @return the openUid
	 */
	public String getOpenUid() {
		return openUid;
	}

	/**
	 * @param openUid the openUid to set
	 */
	public SlThirdLoginDto setOpenUid(String openUid) {
		this.openUid = openUid;
		return this;
	}

	/**
	 * @return the loginType
	 */
	public Short getLoginType() {
		return loginType;
	}

	/**
	 * @param loginType the loginType to set
	 */
	public SlThirdLoginDto setLoginType(Short loginType) {
		this.loginType = loginType;
		return this;
	}

	/**
	 * @return the bindAid
	 */
	public BigDecimal getBindAid() {
		return bindAid;
	}

	/**
	 * @param bindAid the bindAid to set
	 */
	public SlThirdLoginDto setBindAid(BigDecimal bindAid) {
		this.bindAid = bindAid;
		return this;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public SlThirdLoginDto setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public SlThirdLoginDto setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		return this;
	}

	/**
	 * @return the expiresIn
	 */
	public String getExpiresIn() {
		return expiresIn;
	}

	/**
	 * @param expiresIn the expiresIn to set
	 */
	public SlThirdLoginDto setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
		return this;
	}
	
	/**.
	 * 判断是否过期了
	 * @return 布尔值
	 */
	public boolean isExpired() {
	    try {
	      long now = System.currentTimeMillis();
	      long expires = Long.parseLong(expiresIn);
	      return (now > expires);
	    } catch (Exception e) {
	      // return expired if expiresIn is null or invalid
	      return true;
	    }
	  }
}