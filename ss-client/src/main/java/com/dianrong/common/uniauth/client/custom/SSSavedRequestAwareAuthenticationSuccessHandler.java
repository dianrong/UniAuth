package com.dianrong.common.uniauth.client.custom;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.dianrong.common.uniauth.common.cons.AppConstants;

public class SSSavedRequestAwareAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private RequestCache requestCache = new HttpSessionRequestCache();
	
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

		// Use the DefaultSavedRequest URL
		List<String> ajaxHeaderValueList = savedRequest.getHeaderValues(AppConstants.AJAS_HEADER);
		if(ajaxHeaderValueList == null || ajaxHeaderValueList.isEmpty()){
			String targetUrl = savedRequest.getRedirectUrl();
			logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}
		else{
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
}
