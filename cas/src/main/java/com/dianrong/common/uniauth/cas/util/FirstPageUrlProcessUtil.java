package com.dianrong.common.uniauth.cas.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

/**.
 * 登陆主页参数记录的工具方法
 * @author wanglin
 *
 */
public class FirstPageUrlProcessUtil {
		
	/**.
	 * 刷新session中保存的主页会跳地址的参数
	 * @param request  HttpServletRequest
	 */
	public static void refreshLoginContextInsession(HttpServletRequest request){
		if(request == null){
			throw new NullPointerException("HttpServletRequest can not be null");
		}
		// 记录跳转的上下文url
		String savedLoginContext = request.getParameter(AppConstants.PWDFORGET_DISPATCHER_CONTEXTURL_KEY);
		//刷新主页登陆跳转的参数
		if (!StringUtil.strIsNullOrEmpty(savedLoginContext)) {
			HttpSession session = request.getSession(false);
			//过滤问号
			if(savedLoginContext.startsWith("?")){
				savedLoginContext = savedLoginContext.substring(1);
			}
			
			putValToSession(session, AppConstants.PWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY,  savedLoginContext);
		}
	}
	
	/**
	 * . set object to session
	 * 
	 * @param session
	 * @param key
	 * @param val
	 * @return
	 */
	private static boolean putValToSession(HttpSession session, String key, Object val) {
		if (session == null) {
			return false;
		}
		session.setAttribute(key, val);
		return true;
	}
}
