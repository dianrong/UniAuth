package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.service.ForgetPasswordService;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.exp.UniauthException;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Slf4j
public class ForgetPasswordController extends AbstractController {

  /**
   * 定义在找回密码操作中的一些请求参数的KEY.
   */
  private static final String PARAMETER_STEP_KEY = "step";
  private static final String REQUEST_CAPTCHA_KEY = "captcha";
  private static final String IDENTITY_KEY = "identity";
  private static final String NEW_PSWD_KEY = "newPassword";

  // 定义找回密码的每一个步骤
  /**
   * 输入身份标识信息.邮箱或者电话号码.
   */
  private static final String STEP_0 = "0";

  /**
   * 与Step0一致,但需要输入验证码.
   */
  private static final String STEP_1 = "1";

  /**
   * 根据邮件或短信来验证身份.
   */
  private static final String STEP_2 = "2";

  /**
   * 设置新密码.
   */
  private static final String STEP_3 = "3";

  /**
   * 密码设置成功.
   */
  private static final String STEP_4 = "4";

  /**
   * 处理找回密码的Service.
   */
  private ForgetPasswordService forgetPasswordService;

  @Override
  protected ModelAndView handleRequestInternal(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    String step = WebScopeUtil.getParamFromRequest(request, PARAMETER_STEP_KEY);
    if (StringUtil.strIsNullOrEmpty(step)
        || (!isHandleRequest(request) && !isQueryPageRequest(request))) {
      return null;
    }

    // step0
    if (STEP_0.equals(step)) {
      return toStep1(request, response);
    }

    // step1
    if (STEP_1.equals(step)) {
      handleStep1(request, response);
    }

    // step2
    if (STEP_2.equals(step)) {
      if (isQueryPageRequest(request)) {
        return toStep2(request, response);
      }
    }

    // step3
    if (STEP_3.equals(step)) {
      if (isQueryPageRequest(request)) {
        return toStep3(request, response);
      } else {
        handleStep3(request, response);
      }
    }

    // step4
    if (STEP_4.equals(step)) {
      return toStep4(request, response);
    }

    return null;
  }

  /**
   * To step1 page.
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
    // 必须要有身份标识信息.
    String identity = WebScopeUtil.getIdentity(session);
    if (StringUtil.strIsNullOrEmpty(identity)) {
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
    String identity = WebScopeUtil.getIdentity(session);
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

    String identity = WebScopeUtil.getIdentity(session);
    // clear session
    clearAllSessionVal(session);

    WebScopeUtil.putIndentity(session, StringUtils.hasText(identity) ? identity : "");
    return getPwdForgetStep4Page();
  }

  /**
   * Handle step1.
   */
  private void handleStep1(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 验证验证码
    String inputCaptcha = WebScopeUtil.getParamFromRequest(request, REQUEST_CAPTCHA_KEY);
    if (StringUtil.strIsNullOrEmpty(inputCaptcha)) {
      // 验证码为空了
      responseJson(response, AjaxResult.CODE_1);
      return;
    } else {
      // 判断验证码
      if (!WebScopeUtil.checkCaptchaFromSession(session, inputCaptcha)) {
        responseJson(response, AjaxResult.CODE_2);
        return;
      }
    }

    // 从request中获取邮箱或者手机号
    String identity = WebScopeUtil.getParamFromRequest(request, IDENTITY_KEY);
    // 返回step 1
    if (StringUtil.strIsNullOrEmpty(identity)) {
      // email 为空了
      responseJson(response, AjaxResult.CODE_3);
      return;
    }
    String tenancyCode =
        WebScopeUtil.getParamFromRequest(request, CasConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);
    UserDto user = null;
    try {
      user = forgetPasswordService.checkUser(identity, tenancyCode);
      Assert.notNull(user,
          "can not find user, email or phone :" + identity + ", tenancyCode:" + tenancyCode);
    } catch (UniauthException ex) {
      log.debug("Failed to check user", ex);
      responseJson(response, AjaxResult.CODE_4, ex.getMessage());
      return;
    } catch (Exception ex) {
      log.error("Failed to check user", ex);
      responseJson(response, AjaxResult.CODE_4, CasConstants.SERVER_PROCESS_ERROR);
      return;
    }

    // 往session里面放email
    WebScopeUtil.putIndentity(session, identity);
    WebScopeUtil.putTenancyId(session, StringUtil.translateIntegerToLong(user.getTenancyId()));
    // 成功进入第二步
    responseJson(response, AjaxResult.CODE_0);
  }

  /**
   * Handler step3.
   */
  private void handleStep3(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    HttpSession session = request.getSession(false);
    // 必须要有邮箱or phone number
    String identity = WebScopeUtil.getIdentity(session);
    Long tenancyId = WebScopeUtil.getTenancyId(session);
    if (StringUtil.strIsNullOrEmpty(identity) || tenancyId == null) {
      responseJson(response, AjaxResult.CODE_1);
      return;
    }

    // 新密码
    String newPwd = WebScopeUtil.getParamFromRequest(request, NEW_PSWD_KEY);
    if (StringUtil.strIsNullOrEmpty(newPwd) || tenancyId == null) {
      responseJson(response, AjaxResult.CODE_2);
      return;
    }

    // 必须要邮箱验证码通过
    if (!WebScopeUtil.getVerificationIsChecked(session, identity)) {
      responseJson(response, AjaxResult.CODE_4);
    }
    // 后端修改密码
    try {
      forgetPasswordService.resetPasswordByIdentity(identity, tenancyId, newPwd);
    } catch (UniauthException ex) {
      log.debug("Failed to update user password", ex);
      responseJson(response, AjaxResult.CODE_3, ex.getMessage());
      return;
    } catch (Exception ex) {
      log.error("Failed to update user password", ex);
      responseJson(response, AjaxResult.CODE_3, CasConstants.SERVER_PROCESS_ERROR);
      return;
    }
    // 修改成功
    responseJson(response, AjaxResult.CODE_0);
  }

  /**
   * To step3 page.
   */
  private ModelAndView getPwdForgetStep1Page() {
    ModelAndView step1Page = new ModelAndView("dianrong/forgetpwd/inputEmailView");
    return step1Page;
  }

  /**
   * To step2 page.
   */
  private ModelAndView getPwdForgetStep2Page() {
    return new ModelAndView("dianrong/forgetpwd/getEmailVerifyCodeView");
  }

  /**
   * To step3 page.
   */
  private ModelAndView getPwdForgetStep3Page() {
    return new ModelAndView("dianrong/forgetpwd/resetPasswordView");
  }

  /**
   * To step4 page.
   */
  private ModelAndView getPwdForgetStep4Page() {
    return new ModelAndView("dianrong/forgetpwd/resetPwdSuccess");
  }

  /**
   * Clear all session parameter about password forget.
   */
  private void clearAllSessionVal(HttpSession session) {
    if (session != null) {
      WebScopeUtil.removeCaptchaFromSession(session);
      WebScopeUtil.removeIdentity(session);
      WebScopeUtil.removeTenancyId(session);
    }
  }

  /**
   * 发送JSON格式信息到输出流.
   */
  private void responseJson(HttpServletResponse response, String code) {
    try {
      response.getWriter().write(getJson(code));
    } catch (IOException e) {
      log.error("send ajax json exception:" + e.getMessage());
    }
  }

  /**
   * 发送JSON格式信息到输出流.
   */
  private void responseJson(HttpServletResponse response, String code, String msg) {
    try {
      response.getWriter().write(getJson(code, true, msg));
    } catch (IOException e) {
      log.error("send ajax json exception:" + e.getMessage());
    }
  }

  private String getJson(String code) {
    return getJson(code, true, "");
  }

  private String getJson(String code, boolean success, String msg) {
    AjaxResult result = new AjaxResult();
    result.setCode(code);
    result.setMsg(msg);
    result.setSuccess(success);
    return JsonUtil.object2Jason(result);
  }

  @Getter
  @Setter
  @ToString
  private static final class AjaxResult {
    /**
     * 结果编码0. 标识操作成功.
     */
    private static final String CODE_0 = "0";
    private static final String CODE_1 = "1";
    private static final String CODE_2 = "2";
    private static final String CODE_3 = "3";
    private static final String CODE_4 = "4";
    private String code;
    private boolean success;
    private String msg;
  }

  /**
   * GET请求?
   */
  private boolean isQueryPageRequest(HttpServletRequest request) {
    if ("GET".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    return false;
  }

  /**
   * POST请求?
   */
  private boolean isHandleRequest(HttpServletRequest request) {
    if ("POST".equalsIgnoreCase(request.getMethod())) {
      return true;
    }
    return false;
  }

  public ForgetPasswordService getForgetPasswordService() {
    return forgetPasswordService;
  }

  public void setForgetPasswordService(ForgetPasswordService forgetPasswordService) {
    this.forgetPasswordService = forgetPasswordService;
  }
}
