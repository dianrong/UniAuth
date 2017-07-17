package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.service.ForgetPasswordService;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;

public class ForgetPasswordController extends AbstractBaseController {

  private ForgetPasswordService forgetPasswordService;

  /**
   * 进入第一页开始验证 清空所有的验证缓存.
   */
  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    // judge by step parameter
    // it's this controller which guarantees the security of reseting
    // password, so need store the status of each step, in session
    // in other words, you yourself maintain the status, and need check
    // previous step when performing next step
    // cannot invalidate session at the end of this process, because this
    // will affect cas server, they are sharing a same jsessionid.
    String step = getParamFromRequest(request, AppConstants.PSWDFORGET_DISPATCHER_STEP_KEY);
    String method = request.getMethod();
    if (StringUtil.strIsNullOrEmpty(step) || (!"post".equalsIgnoreCase(method) && !("get")
        .equalsIgnoreCase(method))) {
      return null;
    }

    // step0
    if ("0".equals(step)) {
      return toStep1(request, response);
    }

    // step1
    if ("1".equals(step)) {
      handleStep1(request, response);
    }

    // step2
    if ("2".equals(step)) {
      if ("get".equalsIgnoreCase(method)) {
        return toStep2(request, response);
      }
    }

    // step3
    if ("3".equals(step)) {
      if ("get".equalsIgnoreCase(method)) {
        return toStep3(request, response);
      } else {
        handleStep3(request, response);
      }
    }

    // step4
    if ("4".equals(step)) {
      // 所有method全部提供一样实现
      return toStep4(request, response);
    }

    return null;
  }

  /**
   * Process step1.
   */
  private ModelAndView toStep1(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // clear session
    clearAllSessionVal(session);
    // refresh or set
    return getPwdForgetStep1Page();
  }

  /**
   * Process step2.
   */
  private ModelAndView toStep2(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 必须要有邮箱
    String email = getValFromSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY, String.class);
    if (StringUtil.strIsNullOrEmpty(email)) {
      return getPwdForgetStep1Page();
    }
    return getPwdForgetStep2Page();
  }

  /**
   * Process step3.
   */
  private ModelAndView toStep3(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 必须要有账号
    String identity = getValFromSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY,
        String.class);
    if (StringUtil.strIsNullOrEmpty(identity)) {
      return getPwdForgetStep1Page();
    }

    // 必须要邮箱验证码通过
    if (!WebScopeUtil.getVerificationIsChecked(session, identity)) {
      return getPwdForgetStep2Page();
    }
    // 进入第三步
    return getPwdForgetStep3Page();
  }

  /**
   * Process step4.
   */
  private ModelAndView toStep4(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);

    String email = getValFromSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY, String.class);
    // clear session
    clearAllSessionVal(session);

    // 保存一下当前修改完成密码的邮箱地址到session中
    putValToSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY,
        StringUtil.strIsNullOrEmpty(email) ? "" : email);
    return getPwdForgetStep4Page();
  }

  /**
   * Handler step1.
   */
  private void handleStep1(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 验证验证码
    String verifyCode = getParamFromRequest(request,
        AppConstants.PSWDFORGET_PAGE_VERIFY_CODE_CLIENT_KEY);
    if (StringUtil.strIsNullOrEmpty(verifyCode)) {
      // 验证码为空了
      setResponseResultJson(response, "1");
      return;
    } else {
      // 判断验证码
      String captcha = getValFromSession(session, AppConstants.CAS_CAPTCHA_SESSION_KEY,
          String.class);
      if (!verifyCode.equals(captcha)) {
        // 验证码不对
        setResponseResultJson(response, "2");
        return;
      }
    }

    // 从request中获取邮箱或者手机号
    String identity = getParamFromRequest(request, AppConstants.PSWDFORGET_MAIL_VAL_CLIENT_KEY);
    // 返回step 1
    if (StringUtil.strIsNullOrEmpty(identity)) {
      // email 为空了
      setResponseResultJson(response, "3");
      return;
    }
    String tenancyCode = getParamFromRequest(request,
        AppConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);
    UserDto user = null;
    try {
      // 验证邮箱是否存在
      user = forgetPasswordService.checkUser(identity, tenancyCode);
      Assert.notNull(user,
          "can not find user, email or phone :" + identity + ", tenancyCode:" + tenancyCode);
    } catch (Exception ex) {
      // 验证用户失败了
      setResponseResultJson(response, "4", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
      return;
    }

    // 往session里面放email
    putValToSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY, identity);
    putValToSession(session, AppConstants.PSWDFORGET_TENAYC_ID_KEY,
        StringUtil.translateIntegerToLong(user.getTenancyId()));
    // 成功进入第二步
    setResponseResultJson(response, "0");
  }

  /**
   * Handler step3.
   */
  private void handleStep3(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 必须要有邮箱or phone number
    String identity = getValFromSession(session, AppConstants.PSWDFORGET_MAIL_VAL_KEY,
        String.class);
    Long tenancyId = getValFromSession(session, AppConstants.PSWDFORGET_TENAYC_ID_KEY, Long.class);
    if (StringUtil.strIsNullOrEmpty(identity)) {
      setResponseResultJson(response, "1");
      return;
    }

    // 新密码
    String newPwd = getParamFromRequest(request, AppConstants.PSWDFORGET_NEW_PSWD_KEY);
    if (StringUtil.strIsNullOrEmpty(newPwd) || tenancyId == null) {
      setResponseResultJson(response, "2");
      return;
    }

    // 必须要邮箱验证码通过
    if (!WebScopeUtil.getVerificationIsChecked(session, identity)) {
      setResponseResultJson(response, "4");
    }
    // 后端修改密码
    try {
      forgetPasswordService.resetPasswordByIdentity(identity, tenancyId, newPwd);
    } catch (Exception ex) {
      setResponseResultJson(response, "3", StringUtil.getExceptionSimpleMessage(ex.getMessage()));
      return;
    }
    // 修改成功
    setResponseResultJson(response, "0");
  }

  /**
   * Get Pwdforget step1 page.
   */
  private ModelAndView getPwdForgetStep1Page() {
    ModelAndView step1Page = new ModelAndView("dianrong/forgetpwd/inputEmailView");
    return step1Page;
  }

  /**
   * Get Pwdforget step2 page.
   */
  private ModelAndView getPwdForgetStep2Page() {
    return new ModelAndView("dianrong/forgetpwd/getEmailVerifyCodeView");
  }

  /**
   * Get pwdforget step3 page.
   */
  private ModelAndView getPwdForgetStep3Page() {
    return new ModelAndView("dianrong/forgetpwd/resetPasswordView");
  }

  /**
   * Get pwdforget step4 page.
   */
  private ModelAndView getPwdForgetStep4Page() {
    return new ModelAndView("dianrong/forgetpwd/resetPwdSuccess");
  }

  /**
   * Clear all session parameter about password forget.
   */
  private void clearAllSessionVal(HttpSession session) {
    if (session != null) {
      session.removeAttribute(AppConstants.PSWDFORGET_MAIL_VAL_KEY);

      session.removeAttribute(AppConstants.CAS_CAPTCHA_SESSION_KEY);

      session.removeAttribute(AppConstants.PSWDFORGET_MAIL_VERIFY_EXPIRDATE_KEY);
    }
  }

  // 服务对象
  public ForgetPasswordService getForgetPasswordService() {
    return forgetPasswordService;
  }

  public void setForgetPasswordService(ForgetPasswordService forgetPasswordService) {
    this.forgetPasswordService = forgetPasswordService;
  }
}
