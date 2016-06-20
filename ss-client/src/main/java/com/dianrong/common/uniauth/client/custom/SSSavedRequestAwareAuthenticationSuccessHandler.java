package com.dianrong.common.uniauth.client.custom;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.curator.utils.ZookeeperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;

public class SSSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Autowired
	private DomainDefine domainDefine;
	@Autowired
	private ZooKeeperConfig zooKeeperConfig;
	
	public SSSavedRequestAwareAuthenticationSuccessHandler() {
		
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
		String customiedSavedRequestUrl= domainDefine.getCustomizedSavedRequestUrl();
		if(StringUtils.hasText(customiedSavedRequestUrl)){
			logger.debug("Redirecting to CustomiedSavedRequest Url: " + customiedSavedRequestUrl);
			getRedirectStrategy().sendRedirect(request, response, zooKeeperConfig.getDomainUrl() + customiedSavedRequestUrl);
		}
		else{
			// Use the DefaultSavedRequest URL
			if(!HttpRequestUtil.isAjaxRequest(request) && !HttpRequestUtil.isCORSRequest(request)){
				String targetUrl = savedRequest.getRedirectUrl();
				logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
				getRedirectStrategy().sendRedirect(request, response, targetUrl);
			}
			else{
				super.onAuthenticationSuccess(request, response, authentication);
			}
		}
	}
}
