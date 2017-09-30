package com.dianrong.common.uniauth.cas.util;

import com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel;
import com.dianrong.common.uniauth.cas.model.ExpiredSessionObj;
import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.model.IdentityExpiredSessionObj;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.Base64;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.util.StringUtils;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.net.URLDecoder;

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
    return putValToSession(session, CasConstants.CAS_CAPTCHA_SESSION_KEY, captcha);
  }

  /**
   * Get captcha from session.
   */
  public static String getCaptchaFromSession(HttpSession session) {
    return getValFromSession(session, CasConstants.CAS_CAPTCHA_SESSION_KEY);
  }

  /**
   * Remove captcha from session.
   */
  public static void removeCaptchaFromSession(HttpSession session) {
    removeValFromSession(session, CasConstants.CAS_CAPTCHA_SESSION_KEY);
  }

  /**
   * 验证验证码是否正确.
   */
  public static boolean checkCaptchaFromSession(HttpSession session, String inputCapcha) {
    return checkCaptchaFromSession(session, inputCapcha, true);
  }

  /**
   * 验证验证码是否正确.
   */
  public static boolean checkCaptchaFromSession(HttpSession session, String inputCapcha,
      boolean ignoreCase) {
    String realCaptcha = getCaptchaFromSession(session);
    // remove captcha
    removeCaptchaFromSession(session);
    if (!StringUtils.hasText(realCaptcha)) {
      return false;
    }
    if (ignoreCase) {
      return realCaptcha.equalsIgnoreCase(inputCapcha);
    } else {
      return realCaptcha.equals(inputCapcha);
    }
  }

  /**
   * Set captchaInfo to session.
   */
  public static boolean putCaptchaInfoToSession(HttpSession session,
      CasLoginCaptchaInfoModel captchaInfo) {
    return putValToSession(session, CasConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY,
        captchaInfo);
  }

  /**
   * Get captchaInfo from session.
   */
  public static CasLoginCaptchaInfoModel getCaptchaInfoFromSession(HttpSession session) {
    return getValFromSession(session, CasConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY);
  }

  // SMS
  /**
   * Set SMS Verification Code to session.
   *
   * @return true or false
   */
  public static boolean putSmsVerificationToSession(HttpSession session,
      IdentityExpiredSessionObj<String> verification) {
    return putValToSession(session, CasConstants.SMS_VERIFICATION_SESSION_KEY, verification);
  }

  /**
   * Get SMS Verification Code from session.
   */
  public static IdentityExpiredSessionObj<String> getSmsVerificationFromSession(
      HttpSession session) {
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
  public static IdentityExpiredSessionObj<String> getEmailVerificationFromSession(
      HttpSession session) {
    return getValFromSession(session, CasConstants.EMAIL_VERIFICATION_SESSION_KEY);
  }

  /**
   * Remove email verification from session.
   */
  public static void removeEmailVerification(HttpSession session) {
    session.removeAttribute(CasConstants.EMAIL_VERIFICATION_SESSION_KEY);
  }

  /**
   * Get identity from session.
   */
  public static String getIdentity(HttpSession session) {
    return getValFromSession(session, CasConstants.CAS_USER_IDENTITY);
  }

  /**
   * Set identity into session.
   */
  public static void putIndentity(HttpSession session, String identity) {
    putValToSession(session, CasConstants.CAS_USER_IDENTITY, identity);
  }

  /**
   * Remove identity into session.
   */
  public static void removeIdentity(HttpSession session) {
    removeValFromSession(session, CasConstants.CAS_USER_IDENTITY);
  }

  /**
   * Get tenancyId from session.
   */
  public static Long getTenancyId(HttpSession session) {
    return getValFromSession(session, CasConstants.CAS_USER_TENANCY_ID);
  }

  /**
   * Set tenancyId into session.
   */
  public static void putTenancyId(HttpSession session, Long tenancyId) {
    putValToSession(session, CasConstants.CAS_USER_TENANCY_ID, tenancyId);
  }

  /**
   * Remove tenancyId from session.
   */
  public static void removeTenancyId(HttpSession session) {
    removeValFromSession(session, CasConstants.CAS_USER_TENANCY_ID);
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
    putValToSession(session, sessionKey, ExpiredSessionObj
        .build(Boolean.TRUE, CasConstants.VERIFICATION_SUCCESS_ALIVE_MINUTES * 60L * 1000L));
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
    ExpiredSessionObj<Boolean> checked = getValFromSession(session, sessionKey);
    if (checked != null && !checked.isExpired() && checked.getContent()) {
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
  public static <T> T getValFromSession(HttpSession session, String key, Class<T> clz) {
    if (session == null) {
      return null;
    }
    return (T) session.getAttribute(key);
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
   * Remove object from session.
   */
  public static <T> void removeValFromSession(HttpSession session, String key) {
    if (session == null) {
      return;
    }
    session.removeAttribute(key);
  }

  /**
   * 以JSON格式返回结果.
   *
   * @param obj 返回结果对象
   */
  public static void sendJsonToResponse(HttpServletResponse response, HttpResponseModel<?> obj) {
    response.setContentType(ContentType.APPLICATION_JSON.toString());
    if (obj == null) {
      writeJsonContentToResponse(response, "");
    } else {
      writeJsonContentToResponse(response, JsonUtil.object2Jason(obj));
    }
  }

  /**
   * Write JSON to response stream.
   *
   * @param jsonContent JSON to be write to response stream
   */
  public static void writeJsonContentToResponse(ServletResponse response, String jsonContent) {
    try {
      // 设置header Content-Type:application/json;charset=UTF-8
      response.setContentType(ContentType.APPLICATION_JSON.toString());
      response.getWriter().write(jsonContent);
      response.flushBuffer();
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

  public static String getStringFromScope(@NotNull final RequestContext context,
      @NotNull final String key) {
    final String jwtFromRequest = (String) context.getRequestScope().get(key);
    final String jwtFromFlow = (String) context.getFlowScope().get(key);
    return jwtFromRequest != null ? jwtFromRequest : jwtFromFlow;
  }
}
