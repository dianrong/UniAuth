package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.common.util.StringUtil;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * . 点融扩展的基础的controller
 *
 * @author R9GBP97
 */
@Slf4j
public abstract class AbstractBaseController extends AbstractController {

  /**
   * Get parameter from request.
   *
   * @param request httpRequest
   * @param key parameterKey
   * @return value
   */
  protected String getParamFromRequest(HttpServletRequest request, String key) {
    return request.getParameter(key);
  }

  /**
   * Get object from session.
   *
   * @param session HttpSession
   * @param key key
   * @param clsT classType
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
   * Get object from session.
   *
   * @param session HttpSession
   * @param key key
   * @param clsT classType
   * @return Object
   */
  protected Object getValFromSession(HttpSession session, String key) {
    if (session == null) {
      return null;
    }
    return session.getAttribute(key);
  }

  /**
   * Set object to session.
   */
  protected boolean putValToSession(HttpSession session, String key, Object val) {
    if (session == null) {
      return false;
    }
    session.setAttribute(key, val);
    return true;
  }

  /**
   * Set AJAX result JSON to response.
   */
  protected void setResponseResultJson(HttpServletResponse response, String code) {
    try {
      response.getWriter().write(getAjaxJson(code));
    } catch (IOException e) {
      log.error("send ajax json exception:" + e.getMessage());
    }
  }

  /**
   * Set AJAX result JSON to response.
   */
  protected void setResponseResultJson(HttpServletResponse response, String code, String msg) {
    try {
      response.getWriter().write(getAjaxJson(code, true, msg));
    } catch (IOException e) {
      log.error("send ajax json exception:" + e.getMessage());
    }
  }

  /**
   * Manual package a JSON string.
   *
   * @param code code
   */
  protected String getAjaxJson(String code) {
    return getAjaxJson(code, true, "");
  }

  /**
   * Manual package a JSON string.
   *
   * @param code code
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
