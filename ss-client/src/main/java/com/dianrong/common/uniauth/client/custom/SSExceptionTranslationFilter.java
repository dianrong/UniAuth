package com.dianrong.common.uniauth.client.custom;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.RequestCache;

import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SSExceptionTranslationFilter extends ExceptionTranslationFilter {
	@Autowired
	private ZooKeeperConfig zooKeeperConfig;
	
	@Autowired(required = false)
	private CustomizedRedirectFormat customizedRedirectFormat;
	
	public SSExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint, RequestCache requestCache) {
		super(authenticationEntryPoint, requestCache);
	}

	public SSExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationEntryPoint);
	}

	protected void sendStartAuthentication(HttpServletRequest request,	HttpServletResponse response, FilterChain chain, AuthenticationException reason) throws ServletException, IOException {
		// SEC-112: Clear the SecurityContextHolder's Authentication, as the
		// existing Authentication is no longer considered valid
		SecurityContextHolder.getContext().setAuthentication(null);
		if(HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCORSRequest(request)){
			log.debug("This ia an ajax or cors request, return json to client side.");
			
			String casServerUrl = zooKeeperConfig.getCasServerUrl();
			String domainUrl = zooKeeperConfig.getDomainUrl();
			domainUrl += "/login/cas";
			domainUrl = HttpRequestUtil.encodeUrl(domainUrl);
			casServerUrl = casServerUrl.endsWith("/") ? casServerUrl + "login" : casServerUrl + "/login";
			String loginUrl = casServerUrl + "?service=" + domainUrl;
			
			response.setContentType("application/json");
			response.addHeader("Cache-Control", "no-store");
			response.setStatus(200);
			
			if(customizedRedirectFormat == null){
				response.getWriter().println("{");
				response.getWriter().println("\"info\":");
				response.getWriter().println("[");
				response.getWriter().println("{");
				response.getWriter().println("\"name\": \"" + AppConstants.LOGIN_REDIRECT_URL +"\",");
				response.getWriter().println("\"msg\": \"" + loginUrl +"\"");
				response.getWriter().println("}");
				response.getWriter().println("]");
				response.getWriter().println("}");
			} else {
				Object redirectObj = customizedRedirectFormat.getRedirectInfo(request, loginUrl);
				if(redirectObj != null){
					response.getWriter().println(JsonUtil.object2Jason(redirectObj));
				}
			}
		}
		else{
			super.sendStartAuthentication(request, response, chain, reason);
		}
	}
}
