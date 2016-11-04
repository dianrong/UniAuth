package org.springframework.security.web.access.regular;

import java.io.IOException;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.log4j.Logger;
import org.springframework.web.filter.GenericFilterBean;

import com.dianrong.common.uniauth.client.custom.LoginUserInfoHolder;
import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.exp.UserNotLoginException;
import com.dianrong.common.uniauth.client.support.ExtractRequestUrl;

/**
 * uniauth中的regular权限的处理filter,必须处理登陆成功的情况
 * 
 * @author wanglin
 */
public class SSRegularPermissionFilter extends GenericFilterBean {
	/**
	 * . logger
	 */
	private static final Logger logger = Logger.getLogger(SSRegularPermissionFilter.class);

	@Override
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
		long start = System.nanoTime();
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;
		boolean checkPass = false;
		try {
			UserExtInfo loginUser = LoginUserInfoHolder.getLoginUserInfo();
			Set<SSRegularPattern> regularPatterns = loginUser.getAllPermittedRegularPattern();
			// no any permission for requesting
			if (regularPatterns.isEmpty()) {
				return;
			}
			String url = ExtractRequestUrl.extractRequestUrl(request, false);
			String requetMethod = request.getMethod();
			for (SSRegularPattern p: regularPatterns) {
				if (p.permissonCheck(requetMethod, url)) {
					checkPass = true;
					break;
				}
			}
		} catch (UserNotLoginException ex) {
			logger.debug("not login", ex);
			checkPass = true;
		} finally {
			try {
			if (checkPass) {
				chain.doFilter(request, response);
			} else {
				unPermittedRequest(request, response);
			}
			} finally {
				logger.debug(this.getClass().getName() +" consume times : " + (System.nanoTime() - start));
			}
		}
	}
	
	/**.
	 * process not permitted request, response code 401
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	private void unPermittedRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.setStatus(HttpStatus.SC_UNAUTHORIZED);
			response.getWriter().write("Sorry! You do not have permission to access the resource!");
		} catch(IOException ex) {
			logger.warn("failed to send unpermitted warn");
		}
	}
}
