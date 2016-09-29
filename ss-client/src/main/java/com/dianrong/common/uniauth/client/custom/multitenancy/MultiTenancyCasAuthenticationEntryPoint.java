package com.dianrong.common.uniauth.client.custom.multitenancy;

import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.enm.CasProtocal;

public class MultiTenancyCasAuthenticationEntryPoint extends CasAuthenticationEntryPoint{
	private String tenancyCode;
	/**.
	 * 重写该方法，加上参数tenancycode
	 */
	@Override
	protected String createRedirectUrl(final String serviceUrl) {
		String redirectUrl = super.createRedirectUrl(serviceUrl);
		if (StringUtils.hasText(tenancyCode)) {
			redirectUrl += redirectUrl.contains("?")?"&":"";
			redirectUrl +=CasProtocal.DianRongCas.getTenancyCodeName()+"=" +  tenancyCode;
		}
		return redirectUrl;
	}

	public String getTenancyCode() {
		return tenancyCode;
	}

	public void setTenancyCode(String tenancyCode) {
		this.tenancyCode = tenancyCode;
	}
	
}
