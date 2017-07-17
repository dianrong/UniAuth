package com.dianrong.common.uniauth.cas.handler;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.servlet.http.HttpSession;
import org.jasig.cas.authentication.AuthenticationException;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.Event;

/**
 * CAS用户登陆的验证码验证帮助类.
 *
 * @author wanglin
 */
public class CasUserLoginCaptchaValidHelper {

  /**
   * 处理失败的id列表.
   */
  private static final List<String> FAILAURE_EVENT_ID_LSIT = new ArrayList<String>();

  /**
   * 处理失败的需要关注的异常class对象列表.
   */
  private static final List<Class<? extends Exception>> FAILAURE_FOCUS_CLASS_LSIT =
      new ArrayList<Class<? extends Exception>>();

  /**
   * 需要当做正常登陆成功处理的异常列表.
   */
  private static final List<Class<? extends Exception>> FAILAURE_CLASS_AS_SUCCESS_LSIT =
      new ArrayList<Class<? extends Exception>>();

  /**
   * 处理成功的id的列表.
   */
  private static final List<String> SUCCESS_EVENT_ID_LSIT = new ArrayList<String>();

  /**
   * 静态数据初始化.
   */
  static {
    // 处理失败
    FAILAURE_EVENT_ID_LSIT.add("authenticationFailure");

    // 初始化异常class对象
    FAILAURE_FOCUS_CLASS_LSIT.add(AccountNotFoundException.class);
    FAILAURE_FOCUS_CLASS_LSIT.add(AccountLockedException.class);
    FAILAURE_FOCUS_CLASS_LSIT.add(UserPasswordNotMatchException.class);

    // 当做成功处理的异常
    FAILAURE_CLASS_AS_SUCCESS_LSIT.add(FreshUserException.class);
    FAILAURE_CLASS_AS_SUCCESS_LSIT.add(CredentialExpiredException.class);

    // 处理成功
    SUCCESS_EVENT_ID_LSIT.add("success");
    SUCCESS_EVENT_ID_LSIT.add("successWithWarnings");
    SUCCESS_EVENT_ID_LSIT.add("warn");
  }

  /**
   * 进行登陆处理前的验证处理.
   *
   * @param messageContext 国际化上下文
   * @param captcha 前端传入的验证码(可能为空,不需要验证码的情况)
   * @return True或者False, 是否继续进行登陆处理
   */
  public boolean captchaValidProcessBefore(HttpSession session, final MessageContext messageContext,
      String captcha) {
    if (session != null) {
      CasLoginCaptchaInfoModel casCaptchaObj = WebScopeUtil.getCaptchaInfoFromSession(session);
      if (casCaptchaObj == null) {
        // 新设置一个
        WebScopeUtil.putCaptchaInfoToSession(session, new CasLoginCaptchaInfoModel());
        return true;
      }

      // 如果存在 则要判断是否需要处理验证码
      if (casCaptchaObj.canLoginWithoutCaptcha()) {
        return true;
      }

      // 验证码不能为空
      if (StringUtil.strIsNullOrEmpty(captcha)) {
        // 设置提示语
        messageContext.addMessage(new MessageBuilder().error()
            .code("screen.cas.userlogin.captcha.validation.empty").build());
        return false;
      }

      // 从session中获取后台生成的验证码
      String serverCaptcha = WebScopeUtil.getCaptchaFromSession(session);
      // 比较验证码
      if (!captcha.equals(serverCaptcha)) {
        messageContext.addMessage(new MessageBuilder().error()
            .code("screen.cas.userlogin.captcha.validation.wrong").build());
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * 登陆处理结束之后,根据登陆结果设置验证码判断相关的数据.
   * @param resultEvent 登陆结果session
   */
  public void captchaValidAfterSubmit(HttpSession session, Event resultEvent) {
    if (session != null) {
      CasLoginCaptchaInfoModel casCaptchaObj = WebScopeUtil.getCaptchaInfoFromSession(session);
      if (casCaptchaObj == null) {
        // 新设置一个
        WebScopeUtil.putCaptchaInfoToSession(session, new CasLoginCaptchaInfoModel());
      }

      // 取出来用
      CasLoginCaptchaInfoModel casLoginCaptcha = WebScopeUtil.getCaptchaInfoFromSession(session);

      // 进行异常类型判断
      if (resultEvent != null) {
        String eventId = resultEvent.getId();
        if (FAILAURE_EVENT_ID_LSIT.contains(eventId)) {
          // 进行异常处理
          Object errorObj = resultEvent.getAttributes().get("error");
          if (errorObj != null) {
            AuthenticationException e = (AuthenticationException) errorObj;
            Collection<Class<? extends Exception>> errorClasses = e.getHandlerErrors().values();
            // 遍历判断是否有需要处理的类型
            if (errorClasses != null && !errorClasses.isEmpty()) {
              for (Class<? extends Exception> tclass : errorClasses) {
                if (FAILAURE_FOCUS_CLASS_LSIT.contains(tclass)) {
                  // 增加一次失败次数
                  casLoginCaptcha.failedCountInc();
                  break;
                }
                if (FAILAURE_CLASS_AS_SUCCESS_LSIT.contains(tclass)) {
                  // 作为登陆成功处理
                  casLoginCaptcha.reInit();
                  break;
                }
              }
            }
          }
        }
        if (SUCCESS_EVENT_ID_LSIT.contains(eventId)) {
          // 登陆成功 清空异常记录
          casLoginCaptcha.reInit();
        }
      }
    }
  }
}
