package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 初始化密码的处理controller.
 *
 * @author wanglin
 */
@Slf4j
@Controller
@RequestMapping("/initPassword")
public class InitPasswordController {

  // Define InitPasswordController process result code
  // Success
  private static final int INITPWDSUCCESSCODE = 0;
  // Failure
  private static final int INITPWDFAILEDCODE = 1;

  /**
   * 用户信息管理服务.
   */
  @Autowired
  private UserInfoManageService userInfoManageService;

  @Autowired
  private MessageSource messageSource;

  /**
   * 跳转到初始化密码处理页面.
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @return password process page
   */
  @RequestMapping(value = "/processPage", method = RequestMethod.GET)
  public String toInitPwdPage(HttpServletRequest request, HttpServletResponse response) {
    return "/dianrong/initpwd/initPwdProcess";
  }

  /**
   * . to success page
   *
   * @return success page
   */
  @RequestMapping(value = "/initSuccess")
  private String toSuccessPage() {
    return "dianrong/initpwd/initPwdSuccess";
  }

  /**
   * 处理密码初始化.
   */
  @RequestMapping(value = "/process", method = RequestMethod.POST)
  protected void processInit(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      // 验证验证码
      String reqVerifycode = WebScopeUtil.getParamFromRequest(request, "verify_code");
      if (StringUtil.strIsNullOrEmpty(reqVerifycode)) {
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.captcha.empty"));
        return;
      }
      
      // 判断验证码
      String captcha = WebScopeUtil.getCaptchaFromSession(request.getSession());
      if (!reqVerifycode.equals(captcha)) {
        // 验证码不对
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.captcha.wrong"));
        return;
      }

      // 获取请求的参数信息
      String reqAccount =
          WebScopeUtil.getParamFromRequest(request, AppConstants.REQUEST_PARAMETER_KEY_EMAIL);
      String reqOriginpwd = WebScopeUtil.getParamFromRequest(request, "originpwd");
      String reqNewpwd = WebScopeUtil.getParamFromRequest(request, "newpwd");
      String reqTenancyCode = WebScopeUtil.getParamFromRequest(request,
          AppConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);

      // 后端验证
      if (StringUtil.strIsNullOrEmpty(reqAccount)) {
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.userpassword.empty"));
        return;
      }
      if (StringUtil.strIsNullOrEmpty(reqOriginpwd)) {
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.originpassword.empty"));
        return;
      }
      if (StringUtil.strIsNullOrEmpty(reqNewpwd)) {
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.newpassword.notempty"));
        return;
      }

      // 根据邮箱获取用户原始信息
      UserDto userinfo = null;
      try {
        userinfo = userInfoManageService.getUserDetailInfo(reqAccount, reqTenancyCode);
      } catch (Exception ex) {
        responseVal(response, false, StringUtil.getExceptionSimpleMessage(ex.getMessage()));
        return;
      }

      // 用户不存在
      if (userinfo == null) {
        responseVal(response, false, UniBundleUtil.getMsg(messageSource,
            "inipassword.controller.initpassword.user.not.exsist", reqAccount));
        return;
      }

      // 修改密码
      try {
        userInfoManageService.updateUserPassword(userinfo.getId(), reqNewpwd, reqOriginpwd);
      } catch (Exception ex) {
        responseVal(response, false, StringUtil.getExceptionSimpleMessage(ex.getMessage()));
        return;
      }
      // 返回修改密码成功
      responseVal(response, true, "");
    } catch (IOException ex) {
      log.error("failed to process user password init", ex);
      try {
        responseVal(response, false, StringUtil.getExceptionSimpleMessage(ex.getMessage()));
      } catch (IOException e) {
        log.warn("failed to process user password init", e);
        throw e;
      }
    }
  }

  /**
   * Easy way for formated response value.
   * @param processSuccess Init password success or failed.
   * @param msg process result message
   * @throws IOException HttpServletResponse IOException
   */
  private void responseVal(HttpServletResponse response, boolean processSuccess, String msg)
      throws IOException {
    HttpResponseModel<Serializable> result = HttpResponseModel.buildSuccessResponse();
    result.setCode(processSuccess ? INITPWDSUCCESSCODE : INITPWDFAILEDCODE);
    result.setMsg(msg);
    // send message
    WebScopeUtil.sendJsonToResponse(response, result);
  }
}
