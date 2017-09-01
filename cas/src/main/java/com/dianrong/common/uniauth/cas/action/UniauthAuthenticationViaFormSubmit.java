package com.dianrong.common.uniauth.cas.action;

import com.dianrong.common.uniauth.cas.action.jwt.JWTCreateAction;
import com.dianrong.common.uniauth.cas.handler.CasUserLoginCaptchaValidHelper;
import com.dianrong.common.uniauth.cas.model.CasUsernamePasswordCredential;
import com.dianrong.common.uniauth.common.util.Assert;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.jasig.cas.authentication.Credential;
import org.jasig.cas.web.flow.AuthenticationViaFormAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * 配置Cas登陆的身份识别以及TGT, JWT等的生成实现.
 *
 * @author wanglin
 */
public class UniauthAuthenticationViaFormSubmit {

  /**
   * 验证码验证失败事件.
   */
  public static final String CAPTCHA_VALIDATION_FAILED = "captchaValidFailed";


  /**
   * JWT生成失败的事件.
   */
  public static final String ERROR = "error";

  /**
   * 进行用户登陆的action.
   */
  private AuthenticationViaFormAction authenticationAction;

  /**
   * 创建JWT.
   */
  private JWTCreateAction jwtCreateAction;

  /**
   * 用户登陆验证码处理帮助类.
   */
  private CasUserLoginCaptchaValidHelper captchaValidHelper;

  /**
   * 所有authenticationAction返回需要生成JWT的结果集合.
   */
  private Set<String> successEventId = new HashSet<String>() {
    private static final long serialVersionUID = 2085189866451204729L;
    {
      add(AuthenticationViaFormAction.SUCCESS);
      add(AuthenticationViaFormAction.SUCCESS_WITH_WARNINGS);
      add(AuthenticationViaFormAction.WARN);
    }
  };

  public UniauthAuthenticationViaFormSubmit(AuthenticationViaFormAction authenticationAction,
      JWTCreateAction jwtCreateAction, CasUserLoginCaptchaValidHelper captchaValidHelper) {
    Assert.notNull(authenticationAction);
    Assert.notNull(jwtCreateAction);
    Assert.notNull(captchaValidHelper);
    this.authenticationAction = authenticationAction;
    this.jwtCreateAction = jwtCreateAction;
    this.captchaValidHelper = captchaValidHelper;
  }

  /**
   * 拦截Realsubmit操作.
   *
   * @param credential 登陆人信息.
   */
  public final Event submit(final RequestContext context, final Credential credential,
      final MessageContext messageContext) {
    // 获取httpSession
    HttpSession httpSession = WebUtils.getHttpServletRequest(context).getSession();

    // 处理不通过
    if (!captchaValidHelper.captchaValidProcessBefore(httpSession, messageContext,
        ((CasUsernamePasswordCredential) credential).getCaptcha())) {
      return new Event(this, CAPTCHA_VALIDATION_FAILED);
    }

    // 调用核心方法进行登陆处理
    Event event = authenticationAction.submit(context, credential, messageContext);

    // 登陆成功
    if (successEventId.contains(event.getId())) {
      if (!jwtCreateAction.doExecute(context)) {
        return new Event(this, ERROR);
      }
    }

    // 进行处理结果的验证处理
    captchaValidHelper.captchaValidAfterSubmit(httpSession, event);

    // 返回结果
    return event;
  }
}
