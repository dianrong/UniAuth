package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.handler.CasUserLoginCaptchaValidHelper;
import com.dianrong.common.uniauth.cas.model.CasUsernamePasswordCredential;
import javax.servlet.http.HttpSession;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * . 配置web-flow的cas登陆流程的submit的拦截过滤操作
 *
 * @author wanglin
 */
public class AuthenticationViaFormSubmitInterceptor {

  /**
   * . 验证码验证失败事件
   */
  public static final String CAPTCHA_VALIDATION_FAILED = "captchaValidFailed";

  /**
   * . 进行用户登陆的action
   */
  private AuthenticationViaFormAction authenticationAction;

  /**
   * . 用户登陆验证码处理帮助类.
   */
  private CasUserLoginCaptchaValidHelper captchaValidHelper;

  /**
   * . 拦截realsubmit操作
   *
   * @param context context
   * @param credential credential
   * @param messageContext messageContext
   * @return 处理结果
   */
  public final Event submit(final RequestContext context, final Credential credential,
      final MessageContext messageContext) {
    // 获取httpSession
    HttpSession httpSession = WebUtils.getHttpServletRequest(context).getSession();

    // 处理不通过
    if (!captchaValidHelper.captchaValidProcessBefore(httpSession, messageContext,
        ((CasUsernamePasswordCredential) credential).getCaptcha())) {
      return newEvent(CAPTCHA_VALIDATION_FAILED);
    }

    // 调用核心方法进行登陆处理
    Event event = authenticationAction.submit(context, credential, messageContext);

    // 进行处理结果的验证处理
    captchaValidHelper.captchaValidAfterSubmit(httpSession, event);

    // 返回结果
    return event;
  }

  /**
   * New event based on the given id.
   *
   * @param id the id
   * @return the event
   */
  private Event newEvent(final String id) {
    return new Event(this, id);
  }

  public AuthenticationViaFormAction getAuthenticationAction() {
    return authenticationAction;
  }

  public void setAuthenticationAction(AuthenticationViaFormAction authenticationAction) {
    this.authenticationAction = authenticationAction;
  }

  public CasUserLoginCaptchaValidHelper getCaptchaValidHelper() {
    return captchaValidHelper;
  }

  public void setCaptchaValidHelper(CasUserLoginCaptchaValidHelper captchaValidHelper) {
    this.captchaValidHelper = captchaValidHelper;
  }
}
