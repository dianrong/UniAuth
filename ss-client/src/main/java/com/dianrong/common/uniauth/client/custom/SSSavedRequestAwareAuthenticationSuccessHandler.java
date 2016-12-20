package com.dianrong.common.uniauth.client.custom;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Autowired
	private DomainDefine domainDefine;
	@Autowired
	private ZooKeeperConfig zooKeeperConfig;
	
	public SSSavedRequestAwareAuthenticationSuccessHandler() {
		
	}

	@PostConstruct
	public void init(){
		if(StringUtils.hasText(domainDefine.getCustomizedLoginRedirecUrl())){
			this.setDefaultTargetUrl(domainDefine.getCustomizedLoginRedirecUrl());
		}
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		SavedRequest savedRequest = requestCache.getRequest(request, response);

		if (savedRequest == null) {
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}
		String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl()
				|| (targetUrlParameter != null && StringUtils.hasText(request
						.getParameter(targetUrlParameter)))) {
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);

			return;
		}

		clearAuthenticationAttributes(request);

		//start to check saved request url 
		String customizedSavedRequestUrl= domainDefine.getCustomizedSavedRequestUrl();
		if(StringUtils.hasText(customizedSavedRequestUrl)){
			log.debug("Redirecting to CustomizedSavedRequest Url: " + customizedSavedRequestUrl);
			// 绝对地址
			if (customizedSavedRequestUrl.startsWith("http")) {
			    getRedirectStrategy().sendRedirect(request, response,  customizedSavedRequestUrl);
			} else {
			    getRedirectStrategy().sendRedirect(request, response, zooKeeperConfig.getDomainUrl() + customizedSavedRequestUrl);
			}
		}
		else{
			// Use the DefaultSavedRequest URL
			if(!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCORSRequest(request)){
				String targetUrl = savedRequest.getRedirectUrl();
				log.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			}
			else{
				super.onAuthenticationSuccess(request, response, authentication);
			}
		}
	}
}
