package com.dianrong.common.uniauth.cas.controller.v2;

import com.dianrong.common.uniauth.cas.controller.support.CasLoginSupport;
import com.dianrong.common.uniauth.cas.exp.InvalidPermissionException;
import com.dianrong.common.uniauth.cas.model.vo.ApiResponse;
import com.dianrong.common.uniauth.cas.model.vo.ResponseCode;
import com.dianrong.common.uniauth.cas.service.LoginManageService;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.common.enm.CasProtocol;
import com.dianrong.common.uniauth.common.exp.NotLoginException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理登录状态的Controller.
 *
 * @author wanglin
 */
@Slf4j
@RestController
@RequestMapping("v2/manage")
public class LoginManageController {

  /**
   * 登陆处理的支持类.
   */
  @Autowired
  private CasLoginSupport loginSupport;

  /**
   * 国际化资源对象.
   */
  @Autowired
  private MessageSource messageSource;

  /**
   * 管理登录的服务.
   */
  @Autowired
  private LoginManageService loginManageService;

  /**
   * 强行将某个账号的SSO登录状态给踢掉. <br> 使用条件: 用户处于登录状态, 用于拥有Techops的超级管理员权限.
   *
   * @param identity 账号
   * @param tenancyCode 租户编码
   */
  @RequestMapping(value = "kick-out-account", method = RequestMethod.POST)
  public ApiResponse<String> kickOutAccount(
      @RequestParam(value = "identity", required = true) String identity,
      @RequestParam(value = "tenancyCode", required = true) String tenancyCode,
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Principal principal = loginSupport.getAuthenticationPrincipal(request, response);
      Long userId = (Long) principal.getAttributes().get(CasProtocol.DianRongCas.getUserIdName());
      loginManageService.kickOutAccount(userId, identity, tenancyCode);
      return ApiResponse.success();
    } catch (NotLoginException e) {
      log.info("user not login", e);
      return ApiResponse.failure(ResponseCode.USER_NOT_LOGIN,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.user.not.login"));
    } catch (InvalidPermissionException e) {
      log.warn("Have no permission to kick out:" + tenancyCode + ":" + identity, e);
      return ApiResponse.failure(ResponseCode.CREATE_SERVICE_TICKET_FAILURE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.manage.no.permission"));
    }
  }

  /**
   * 强行将某个账号的SSO登录状态给踢掉. <br> 使用条件: 用户处于登录状态, 用于拥有Techops的超级管理员权限.
   *
   * @param kickOutUserId 被踢掉用户的id.
   */
  @RequestMapping(value = "kick-out-account", method = RequestMethod.GET)
  public ApiResponse<String> kickOutUser(
      @RequestParam(value = "kickOutUserId", required = true) Long kickOutUserId,
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
      Principal principal = loginSupport.getAuthenticationPrincipal(request, response);
      Long userId = (Long) principal.getAttributes().get(CasProtocol.DianRongCas.getUserIdName());
      loginManageService.kickOutAccount(userId, kickOutUserId);
      return ApiResponse.success();
    } catch (NotLoginException e) {
      log.info("user not login", e);
      return ApiResponse.failure(ResponseCode.USER_NOT_LOGIN,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.user.not.login"));
    } catch (InvalidPermissionException e) {
      log.warn("Have no permission to kick out:" + kickOutUserId, e);
      return ApiResponse.failure(ResponseCode.CREATE_SERVICE_TICKET_FAILURE,
          UniBundleUtil.getMsg(messageSource, "api.v2.login.manage.no.permission"));
    }
  }
}
