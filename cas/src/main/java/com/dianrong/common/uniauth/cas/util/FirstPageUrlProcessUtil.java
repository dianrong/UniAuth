package com.dianrong.common.uniauth.cas.util;

import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * 登陆主页参数记录的工具方法.
 *
 * @author wanglin
 */
@Slf4j
public final class FirstPageUrlProcessUtil {

  /**
   * 刷新session中保存的主页会跳地址的参数.
   */
  public static void refreshLoginContextInsession(HttpServletRequest request) {
    if (request == null) {
      throw new NullPointerException("HttpServletRequest can not be null");
    }
    // 记录跳转的上下文url
    String savedLoginContext = request
        .getParameter(AppConstants.PSWDFORGET_DISPATCHER_CONTEXTURL_KEY);
    // 刷新主页登陆跳转的参数
    if (!StringUtil.strIsNullOrEmpty(savedLoginContext)) {
      HttpSession session = request.getSession();
      // 过滤问号
      if (savedLoginContext.startsWith("?")) {
        savedLoginContext = savedLoginContext.substring(1);
      }

      putValToSession(session, AppConstants.PSWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY,
          savedLoginContext);
    }
  }

  /**
   * 刷新最近用户使用的service值.
   * @param service service值
   */
  public static void refreshServiceInSession(HttpServletRequest request, String service) {
    if (request == null) {
      throw new NullPointerException("HttpServletRequest can not be null");
    }
    HttpSession session = request.getSession(false);
    if (session == null) {
      return;
    }
    if (StringUtils.isEmpty(service)) {
      session.removeAttribute(AppConstants.PSWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY);
    } else {
      try {
        String savedLoginContext = "service=" + URLEncoder.encode(service, "utf-8");
        putValToSession(session, AppConstants.PSWDFORGET_DISPATCHER_CONTEXTURL_SESSION_KEY,
            savedLoginContext);
      } catch (Exception e) {
        // ignore
        log.warn("failed to refreshServiceInSession", e);
      }
    }
  }

  /**
   * Set object to session.
   */
  private static boolean putValToSession(HttpSession session, String key, Object val) {
    if (session == null) {
      return false;
    }
    session.setAttribute(key, val);
    return true;
  }
}
