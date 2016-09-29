package com.dianrong.common.uniauth.client.custom.multitenancy;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.cas.ServiceProperties;
import org.springframework.util.Assert;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.ZkNodeUtils;

public class MultiTenancyServiceProperties extends ServiceProperties{
	private String tenancyCode;
	
	private String domainCode;
	
	@Resource(name="uniauthConfig")
	private Map<String, String> allZkNodeMap;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		setService(getTenancyService());
		super.afterPropertiesSet();
	}
	
	public String getTenancyCode() {
		return tenancyCode;
	}

	public void setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
	}

	public String getDomainCode() {
		return domainCode;
	}

	public void setDomainCode(String domainCode) {
		Assert.notNull(domainCode);
		this.domainCode = domainCode;
	}
	
	private String getTenancyService() {
		boolean isDefault = false;
		if (AppConstants.DEFAULT_TANANCY_CODE.equalsIgnoreCase(tenancyCode)) {
			isDefault = true;
		}
		String serviceUrl = null;
		if (isDefault) {
			serviceUrl = ZkNodeUtils.getDomainUrl(domainCode, tenancyCode, true, allZkNodeMap);
		}
		// try full key
		if (serviceUrl == null) {
			serviceUrl = ZkNodeUtils.getDomainUrl(domainCode, tenancyCode, false, allZkNodeMap);
		}
		if (serviceUrl == null) {
			throw new RuntimeException("tenancyCode:"+tenancyCode+",domainCode:"+domainCode+", there is no serviceurl config in zookeepr");
		}
		return serviceUrl;
	}
}
