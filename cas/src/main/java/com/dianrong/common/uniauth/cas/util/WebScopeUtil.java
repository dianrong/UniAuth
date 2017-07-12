package com.dianrong.common.uniauth.cas.util;

import com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel;
import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.model.IdentityExpiredSessionObj;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.Base64;
import com.dianrong.common.uniauth.common.util.JsonUtil;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;

/**
 * Request, Response, Session等的一些统一操作的工具方法.
 *
 * @author wanglin
 */
@Slf4j
public final class WebScopeUtil {

  private WebScopeUtil() {
    super();
  }

  /**
   * Set captcha to session.
   */
  public static boolean putCaptchaToSession(HttpSession session, String captcha) {
    return putValToSession(session, AppConstants.CAS_CAPTCHA_SESSION_KEY, captcha);
  }

  /**
   * Get captcha from session.
   */
  public static String getCaptchaFromSession(HttpSession session) {
    return getValFromSession(session, AppConstants.CAS_CAPTCHA_SESSION_KEY);
  }

  /**
   * Set captchaInfo to session.
   */
  public static boolean putCaptchaInfoToSession(HttpSession session,
      CasLoginCaptchaInfoModel captchaInfo) {
    return putValToSession(session, AppConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY,
        captchaInfo);
  }

  /**
   * Get captchaInfo from session.
   */
  public static CasLoginCaptchaInfoModel getCaptchaInfoFromSession(HttpSession session) {
    return getValFromSession(session, AppConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY);
  }

  // SMS
  /**
   * Set SMS Verification Code to session.
   * @return true or false
   */
  public static boolean putSmsVerificationToSession(HttpSession session,
      IdentityExpiredSessionObj<String> verification) {
    return putValToSession(session, CasConstants.SMS_VERIFICATION_SESSION_KEY, verification);
  }

  /**
   * Get SMS Verification Code from session.
   */
  public static IdentityExpiredSessionObj<String> getSmsVerificationFromSession(HttpSession session) {
    return getValFromSession(session, CasConstants.SMS_VERIFICATION_SESSION_KEY);
  }

  /**
   * Remove SMS verification Code from session.
   */
  public static void removeSmsVerification(HttpSession session) {
    session.removeAttribute(CasConstants.SMS_VERIFICATION_SESSION_KEY);
  }

  // email
  /**
   * Set email verification Code to session.
   *
   * @return true or false
   */
  public static boolean putEmailVerificationToSession(HttpSession session,
      IdentityExpiredSessionObj<String> verification) {
    return putValToSession(session, CasConstants.EMAIL_VERIFICATION_SESSION_KEY, verification);
  }

  /**
   * Get email verification Code from session.
   *
   * @return verification
   */
  public static IdentityExpiredSessionObj<String> getEmailVerificationFromSession(HttpSession session) {
    return getValFromSession(session, CasConstants.EMAIL_VERIFICATION_SESSION_KEY);
  }

  /**
   * Remove email verification from session.
   */
  public static void removeEmailVerification(HttpSession session) {
    session.removeAttribute(CasConstants.EMAIL_VERIFICATION_SESSION_KEY);
  }

  /**
   * Set a flag to session, represent the identity is verified.
   *
   * @param session can not be null
   * @param identity can not be null
   */
  public static void setVerificationChecked(HttpSession session, String identity) {
    Assert.notNull(session);
    Assert.notNull(identity);
    String sessionKey = Base64.encode(identity.getBytes());
    putValToSession(session, sessionKey, Boolean.TRUE);
  }

  /**
   * Get flag from session, check whether the identity is verified.
   *
   * @param session can not be null
   * @return true or false
   */
  public static boolean getVerificationIsChecked(HttpSession session, String identity) {
    Assert.notNull(session);
    if (!StringUtils.hasText(identity)) {
      return false;
    }
    String sessionKey = Base64.encode(identity.getBytes());
    Boolean checked = getValFromSession(session, sessionKey);
    if (checked != null && checked) {
      return true;
    }
    return false;
  }

  /**
   * Get captchaInfo from session.
   */
  public static boolean loginNeedCaptcha(HttpSession session) {
    CasLoginCaptchaInfoModel captchaInfo = getCaptchaInfoFromSession(session);
    if (captchaInfo != null && !captchaInfo.canLoginWithoutCaptcha()) {
      return true;
    }
    return false;
  }

  /**
   * Set object to session.
   */
  public static boolean putValToSession(HttpSession session, String key, Serializable val) {
    if (session == null) {
      return false;
    }
    session.setAttribute(key, val);
    return true;
  }

  /**
   * Get object from session.
   *
   * @param key key
   * @return Object
   */
  @SuppressWarnings("unchecked")
  public static <T> T getValFromSession(HttpSession session, String key) {
    if (session == null) {
      return null;
    }
    return (T) session.getAttribute(key);
  }


  /**
   * 以JSON格式返回结果.
   *
   * @param obj 返回结果对象
   */
  public static void sendJsonToResponse(HttpServletResponse response, HttpResponseModel<?> obj)
      throws IOException {

    if (obj == null) {
      response.getWriter().write("");
    } else {
      response.getWriter().write(JsonUtil.object2Jason(obj));
    }
  }

  /**
   * Write JSON to response stream.
   * @param jsonContent JSON to be write to response stream
   */
  public static void writeJsonContentToResponse(ServletResponse response, String jsonContent) {
    try {
      // 设置header Content-Type:application/json;charset=UTF-8
      response.setContentType(ContentType.APPLICATION_JSON.toString());
      response.getWriter().write(jsonContent);
    } catch (IOException e) {
      log.warn("response json to client failed", e);
    }
  }

  /**
   * 判断两个service是否是同一个service.
   */
  public static boolean judgeTwoServiceIsEqual(String service1, String service2) {
    if (service1 == null || service2 == null) {
      return false;
    }
    try {
      String turl1 = URLDecoder.decode(service1.trim(), "utf-8");
      String turl2 = URLDecoder.decode(service2.trim(), "utf-8");
      URL url1 = new URL(turl1);
      URL url2 = new URL(turl2);
      int port1 = url1.getPort() != -1 ? url1.getPort()
          : "https".equalsIgnoreCase(url1.getProtocol()) ? 443 : 80;
      int port2 = url2.getPort() != -1 ? url2.getPort()
          : "https".equalsIgnoreCase(url2.getProtocol()) ? 443 : 80;
      if (url1.getProtocol().equals(url2.getProtocol()) && url1.getHost().equals(url2.getHost())
          && port1 == port2) {
        return true;
      }
      return false;
    } catch (Exception e) {
      log.warn("judgeTwoServiceIsEqual failed", e);
      return false;
    }
  }

  /**
   * Get parameter from request.
   *
   * @param key parameterKey
   */
  public static String getParamFromRequest(HttpServletRequest request, String key) {
    return request.getParameter(key);
  }

  /**
   * Set attribute to request.
   *
   * @param key attributeKey.
   * @param value the value to set to httpRequest.
   */
  public static void setAttribute(HttpServletRequest request, String key, Object value) {
    request.setAttribute(key, value);
  }
}
