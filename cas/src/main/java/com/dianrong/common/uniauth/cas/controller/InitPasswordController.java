package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.exp.UniauthException;
import com.dianrong.common.uniauth.common.util.StringUtil;

import java.io.IOException;
import java.io.Serializable;

import javax.security.auth.login.AccountException;
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
   * 密码初始化成功.
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
    // 验证验证码
    String reqVerifyCode = WebScopeUtil.getParamFromRequest(request, "verify_code");
    if (!WebScopeUtil.checkCaptchaFromSession(request.getSession(), reqVerifyCode)) {
      // 验证码不对
      responseVal(response, false,
          UniBundleUtil.getMsg(messageSource, "inipassword.controller.initpassword.captcha.wrong"));
      return;
    }

    // 获取请求的参数信息
    String reqAccount =
        WebScopeUtil.getParamFromRequest(request, CasConstants.REQUEST_PARAMETER_KEY_EMAIL);
    String reqOriginalPwd = WebScopeUtil.getParamFromRequest(request, "originpwd");
    String reqNewPwd = WebScopeUtil.getParamFromRequest(request, "newpwd");
    String reqTenancyCode =
        WebScopeUtil.getParamFromRequest(request, CasConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);

    // 后端验证
    if (StringUtil.strIsNullOrEmpty(reqAccount)) {
      responseVal(response, false, UniBundleUtil.getMsg(messageSource,
          "inipassword.controller.initpassword.userpassword.empty"));
      return;
    }
    if (StringUtil.strIsNullOrEmpty(reqOriginalPwd)) {
      responseVal(response, false, UniBundleUtil.getMsg(messageSource,
          "inipassword.controller.initpassword.originpassword.empty"));
      return;
    }
    if (StringUtil.strIsNullOrEmpty(reqNewPwd)) {
      responseVal(response, false, UniBundleUtil.getMsg(messageSource,
          "inipassword.controller.initpassword.newpassword.notempty"));
      return;
    }

    // 根据邮箱获取用户原始信息
    UserDto userInfo = null;
    try {
      userInfo = userInfoManageService.getUserDetailInfo(reqAccount, reqTenancyCode);
    } catch (UniauthException | AccountException ex) {
      log.debug("Failed to get user detail info", ex);
      responseVal(response, false, ex.getMessage());
      return;
    } catch (Exception ex) {
      log.error("Failed to get user detail info", ex);
      responseVal(response, false, CasConstants.SERVER_PROCESS_ERROR);
      return;
    }

    // 用户不存在
    if (userInfo == null) {
      responseVal(response, false, UniBundleUtil.getMsg(messageSource,
          "inipassword.controller.initpassword.user.not.exsist", reqAccount));
      return;
    }

    // 修改密码
    try {
      userInfoManageService.updateUserPassword(userInfo.getId(), reqNewPwd, reqOriginalPwd);
    } catch (UniauthException | AccountException ex) {
      log.debug("Failed to update user password", ex);
      responseVal(response, false, ex.getMessage());
      return;
    } catch (Exception ex) {
      log.error("Failed to update user password", ex);
      responseVal(response, false, CasConstants.SERVER_PROCESS_ERROR);
      return;
    }

    // 返回修改密码成功
    responseVal(response, true, "");
  }

  /**
   * Easy way for format response value.
   * 
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
