package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.mvc.AbstractController;

import com.dianrong.common.uniauth.common.util.StringUtil;

/**
 * . 点融扩展的基础的controller
 * 
 * @author R9GBP97
 */
public abstract class AbstractBaseController extends AbstractController {
	/**
	 * . get parameter from request
	 * 
	 * @param reqeust
	 *            httpRequest
	 * @param key
	 *            parameterKey
	 * @return val
	 */
	protected String getParamFromRequest(HttpServletRequest reqeust, String key) {
		return reqeust.getParameter(key);
	}

	/**
	 * . get object from session
	 * 
	 * @param session
	 *            HttpSession
	 * @param key
	 *            key
	 * @param clsT
	 *            classType
	 * @return Object
	 */
	@SuppressWarnings("unchecked")
	protected <T> T getValFromSession(HttpSession session, String key, Class<T> clsT) {
		if (session == null) {
			return null;
		}
		return (T) session.getAttribute(key);
	}

	/**
	 * . get object from session
	 * 
	 * @param session
	 *            HttpSession
	 * @param key
	 *            key
	 * @param clsT
	 *            classType
	 * @return Object
	 */
	protected Object getValFromSession(HttpSession session, String key) {
		if (session == null) {
			return null;
		}
		return session.getAttribute(key);
	}

	/**
	 * . set object to session
	 * 
	 * @param session
	 * @param key
	 * @param val
	 * @return
	 */
	protected boolean putValToSession(HttpSession session, String key, Object val) {
		if (session == null) {
			return false;
		}
		session.setAttribute(key, val);
		return true;
	}

	/**
	 * . set ajax result json to response
	 * 
	 * @param response
	 * @param code
	 */
	protected void setResponseResultJson(HttpServletResponse response, String code) {
		try {
			response.getWriter().write(getAjaxJson(code));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * . set ajax result json to response
	 * 
	 * @param response
	 * @param code
	 */
	protected void setResponseResultJson(HttpServletResponse response, String code, String msg) {
		try {
			response.getWriter().write(getAjaxJson(code, true, msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * . manual package a json str
	 * 
	 * @param code
	 *            code
	 * @return
	 */
	protected String getAjaxJson(String code) {
		return getAjaxJson(code, true, "");
	}

	/**
	 * . manual package a json str
	 * 
	 * @param code
	 *            code
	 * @return
	 */
	protected String getAjaxJson(String code, boolean isSucess, String msg) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"issuccess\":");
		sb.append("\"" + (isSucess ? "true" : "flase") + "\"");
		sb.append(",");
		sb.append("\"code\":");
		sb.append("\"" + code + "\"");
		if (!StringUtil.strIsNullOrEmpty(msg)) {
			sb.append(",");
			sb.append("\"msg\":");
			sb.append("\"" + msg + "\"");
		}
		sb.append("}");
		return sb.toString();
	}
}
