package com.dianrong.common.uniauth.common.client;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class DomainDefine implements Serializable {
	
	private static final Logger logger = Logger.getLogger(DomainDefine.class);
	
	private static final long serialVersionUID = 7044166044801306772L;
	private String domainCode;
	private String userInfoClass;
	private boolean rejectPublicInvocations;
	private String customizedSavedRequestUrl; 
	// 自定义登陆成功的跳转url
	private String customizedLoginRedirecUrl;
	private static Integer domainId;
	
	/**
	 * 权限控制类型定义,默认为使用uri_pattern
	 */
	private CasPermissionControlType controlType = CasPermissionControlType.URI_PATTERN;

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		this.domainCode = domainCode;
	}

	public String getUserInfoClass() {
		return userInfoClass;
	}

	public void setUserInfoClass(String userInfoClass) {
		this.userInfoClass = userInfoClass;
	}

	public boolean isRejectPublicInvocations() {
		return rejectPublicInvocations;
	}

	public void setRejectPublicInvocations(boolean rejectPublicInvocations) {
		this.rejectPublicInvocations = rejectPublicInvocations;
	}

	public String getCustomizedSavedRequestUrl() {
		return customizedSavedRequestUrl;
	}

	public void setCustomizedSavedRequestUrl(String customizedSavedRequestUrl) {
		this.customizedSavedRequestUrl = customizedSavedRequestUrl;
	}

	public String getCustomizedLoginRedirecUrl() {
		return customizedLoginRedirecUrl;
	}

	public void setCustomizedLoginRedirecUrl(String customizedLoginRedirecUrl) {
		this.customizedLoginRedirecUrl = customizedLoginRedirecUrl;
	}

	public static Integer getDomainId() {
		return domainId;
	}

	public static void setDomainId(Integer domainId) {
		DomainDefine.domainId = domainId;
	}
	
	public String getControlType() {
		return controlType.getTypeStr();
	}

	/**
	 *  set permission control type
	 * @param type type
	 * @throws IllegalArgumentException if parameter type is not a legal  CasPermissionControlType string
	 */
	public void setControlType(String type) throws IllegalArgumentException{
		try {
			this.controlType = CasPermissionControlType.valueOf(type);
		} catch (IllegalArgumentException ex) {
			logger.debug("illegal argument", ex);
			throw new IllegalArgumentException("permission control type supports [" + CasPermissionControlType.allType()+"], please check the param '" +type+"'");
		}
	}
	
	/**
	 * 判断当前的定义的权限控制模式是否支持指定的类型
	 * @param type 指定类型的权限控制模式
	 * @return true or false
	 */
	public  boolean controlTypeSupport(CasPermissionControlType type) {
		if (type == null) {
			logger.error("controlTypeSupport param is null");
			return false;
		}
		return this.controlType.support(type.getTypeStr());
	}

	/**
	 * cas 的权限控制类型定义
	 * @author wanglin
	 */
	public static enum CasPermissionControlType {
		// default type
		URI_PATTERN("URI_PATTERN", "URI_PATTERN"),
		// 正则类型
		REGULAR("REGULAR", "REGULAR"),
		// 启用所有
		ALL("ALL", "URI_PATTERN", "REGULAR"),
		// 一种都不启用
		NONE("NONE")
		;
		private final String typeStr;
		private final Set<String> supportTypes;
		
		private CasPermissionControlType(String type, String... types) {
			Assert.notNull(type);
			this.typeStr = type;
			this.supportTypes = new HashSet<String>(Arrays.asList(types));
		}
		public String getTypeStr() {
			return typeStr;
		}
		public boolean support(String type) {
			return supportTypes.contains(type);
		}
		/**
		 * get CasPermissionControlType all type string, split with ,
		 * @return types string. eg. ALL, NONE...
		 */
		public static  String allType(){
			StringBuilder sb = new StringBuilder();
			int index = 0;
			for(CasPermissionControlType type : CasPermissionControlType.values()) {
				if (index > 0) {
					sb.append(", ");
				}
				sb.append(type.getTypeStr());
				index++;
			}
			return sb.toString();
		}
	} 
}
