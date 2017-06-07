package com.dianrong.common.uniauth.cas.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dianrong.common.uniauth.cas.model.HttpResponseModel;
import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * . 初始化密码的处理controller
 *
 * @author wanglin
 */
@Controller
@RequestMapping("/initPassword")
@Slf4j
public class InitPasswordController {

  // define InitPasswordController process result code
  // success
  private static final int INITPWDSUCCESSCODE = 0;
  // failure
  private static final int INITPWDFAILEDCODE = 1;

  /**
   * . 用户信息管理服务
   */
  @Autowired
  private UserInfoManageService userInfoManageService;

  @Autowired
  private MessageSource messageSource;

  /**
   * . 跳转到初始化密码处理页面
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
   * 处理密码初始化
   *
   * @param request HttpServletRequest
   * @param response HttpServletResponse
   * @throws IOException HttpServletResponse IOException
   */
  @RequestMapping(value = "/process", method = RequestMethod.POST)
  protected void processInit(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    try {
      // 验证验证码
      String req_verifycode = WebScopeUtil.getParamFromRequest(request, "verify_code");
      if (StringUtil.strIsNullOrEmpty(req_verifycode)) {
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.captcha.empty"));
        return;
      }
      // 判断验证码

      String captcha = WebScopeUtil.getCaptchaFromSession(request.getSession());
      if (!req_verifycode.equals(captcha)) {
        // 验证码不对
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.captcha.wrong"));
        return;
      }

      // 获取请求的参数信息
      String req_account = WebScopeUtil
          .getParamFromRequest(request, AppConstants.REQUEST_PARAMETER_KEY_EMAIL);
      String req_originpwd = WebScopeUtil.getParamFromRequest(request, "originpwd");
      String req_newpwd = WebScopeUtil.getParamFromRequest(request, "newpwd");
      String req_tenancyCode = WebScopeUtil
          .getParamFromRequest(request, AppConstants.REQUEST_PARAMETER_KEY_TENANCY_CODE);

      // 后端验证
      if (StringUtil.strIsNullOrEmpty(req_account)) {
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.userpassword.empty"));
        return;
      }
      if (StringUtil.strIsNullOrEmpty(req_originpwd)) {
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.originpassword.empty"));
        return;
      }
      if (StringUtil.strIsNullOrEmpty(req_newpwd)) {
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.newpassword.notempty"));
        return;
      }

      // 根据邮箱获取用户原始信息
      UserDto userinfo = null;
      try {
        userinfo = userInfoManageService.getUserDetailInfo(req_account, req_tenancyCode);
      } catch (Exception ex) {
        responseVal(response, false, StringUtil.getExceptionSimpleMessage(ex.getMessage()));
        return;
      }

      // 用户不存在
      if (userinfo == null) {
        responseVal(response, false, UniBundleUtil
            .getMsg(messageSource, "inipassword.controller.initpassword.user.not.exsist",
                req_account));
        return;
      }

      // 修改密码
      try {
        userInfoManageService.updateUserPassword(userinfo.getId(), req_newpwd, req_originpwd);
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
   * easy way for formated response value
   *
   * @param response HttpServletResponse
   * @param processSuccess init password success or not
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
