package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.controller.support.CasLoginSupport;
import com.dianrong.common.uniauth.cas.service.UserInfoManageService;
import com.dianrong.common.uniauth.cas.util.UniBundleUtil;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.InfoName;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.enm.CasProtocal;
import com.dianrong.common.uniauth.common.exp.NotLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户信息管理的controller.
 *
 * @author wanglin
 */
@Slf4j
@RestController
@RequestMapping("/userinfo")
public class UserInfoManageController {

  /**
   * . 用户信息管理服务
   */
  @Autowired
  private UserInfoManageService userInfoManageService;

  @Autowired
  private CasLoginSupport casLoginSupport;

  @Autowired
  private MessageSource messageSource;

  /**
   * 更新用户的普通信息,目前只有name.
   *
   * @return 更新结果
   */
  @RequestMapping(value = "update", method = RequestMethod.POST)
  public Response<?> updateInfo(HttpServletRequest request, HttpServletResponse response,
      UserDto user) {
    if (!checkIsLogin(request, response)) {
      return getNotLoginResult();
    }
    UserIdentity userIdentity = getCurrentLoginUserId(request, response);
    try {
      userInfoManageService
          .updateUserInfo(userIdentity.getAccount(), userIdentity.getTenancyId(), user.getName());
    } catch (Exception e) {
      log.error("failed update user's information ", e);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, e.getMessage()));
    }
    return Response.success();
  }

  /**
   * 更新 email 信息.
   */
  @RequestMapping(value = "email", method = RequestMethod.POST)
  public Response<?> updateEmail(HttpServletRequest request, HttpServletResponse response,
      UserDto user) {
    if (!checkIsLogin(request, response)) {
      return getNotLoginResult();
    }
    UserIdentity userIdentity = getCurrentLoginUserId(request, response);
    String identity = user.getEmail();
    // email should verify first
    if (!WebScopeUtil.getVerificationIsChecked(request.getSession(), identity)) {
      log.debug("email verify faild");
      return Response.failure(Info.build(InfoName.BAD_REQUEST, UniBundleUtil
          .getMsg(messageSource, "manage.userinfo.controller.not.verification", "Email")));
    }
    try {
      userInfoManageService
          .updateEmail(userIdentity.getAccount(), userIdentity.getTenancyId(), user.getEmail());
    } catch (Exception e) {
      log.error("failed update user's email ", e);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, e.getMessage()));
    }
    return Response.success();
  }

  /**
   * 更新phone信息.
   */
  @RequestMapping(value = "phone", method = RequestMethod.POST)
  public Response<?> updatePhone(HttpServletRequest request, HttpServletResponse response,
      UserDto user) {
    if (!checkIsLogin(request, response)) {
      return getNotLoginResult();
    }
    UserIdentity userIdentity = getCurrentLoginUserId(request, response);
    String identity = user.getPhone();
    // phone should verify first
    if (!WebScopeUtil.getVerificationIsChecked(request.getSession(), identity)) {
      log.debug("phone verify faild");
      return Response.failure(Info.build(InfoName.BAD_REQUEST, UniBundleUtil
          .getMsg(messageSource, "manage.userinfo.controller.not.verification", "Phone")));
    }
    try {
      userInfoManageService
          .updatePhone(userIdentity.getAccount(), userIdentity.getTenancyId(), user.getPhone());
    } catch (Exception e) {
      log.error("failed update user's phone ", e);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, e.getMessage()));
    }
    return Response.success();
  }

  /**
   * 更新密码.
   */
  @RequestMapping(value = "password", method = RequestMethod.POST)
  public Response<?> updatePassword(HttpServletRequest request, HttpServletResponse response,
      UserDto user,
      @RequestParam(value = "originPassword", required = true) String originPassword) {
    if (!checkIsLogin(request, response)) {
      return getNotLoginResult();
    }
    UserIdentity userIdentity = getCurrentLoginUserId(request, response);
    try {
      userInfoManageService.updatePassword(userIdentity.getAccount(), userIdentity.getTenancyId(),
          user.getPassword(), originPassword);
    } catch (Exception e) {
      log.error("failed update user's password ", e);
      return Response.failure(Info.build(InfoName.BAD_REQUEST, e.getMessage()));
    }
    return Response.success();
  }

  /**
   * 判断当前的请求是否处于登陆状态.
   */
  private boolean checkIsLogin(HttpServletRequest request, HttpServletResponse response) {
    try {
      casLoginSupport.queryTgtWithLogined(request, response);
      return true;
    } catch (NotLoginException ex) {
      log.error("call update userInfo, but not login", ex);
      return false;
    }
  }

  /**
   * 获取当前登陆用户的用户id.
   * @throws NotLoginException 如果没有登陆则抛出该异常
   */
  private UserIdentity getCurrentLoginUserId(HttpServletRequest request,
      HttpServletResponse response) {
    TicketGrantingTicket tgt = casLoginSupport.queryTgtWithLogined(request, response);
    Principal principal = casLoginSupport.getAuthenticationPrincipal(tgt.getId());
    // 获取用户账号
    String account = principal.getId();
    Long tenancyId = (Long) principal.getAttributes()
        .get(CasProtocal.DianRongCas.getTenancyIdName());
    return new UserIdentity(account, tenancyId);
  }

  // 未登陆的结果对象
  private Response<Void> getNotLoginResult() {
    return Response.failure(Info.build(InfoName.BAD_REQUEST,
        UniBundleUtil.getMsg(messageSource, "manage.userinfo.controller.not.login")));
  }

  // 辅助类 用于返回结果
  class UserIdentity {

    private final String account;
    private final long tenancyId;

    public UserIdentity(String account, Long tenancyId) {
      Assert.notNull(account);
      Assert.notNull(tenancyId);
      this.account = account;
      this.tenancyId = tenancyId;
    }

    public String getAccount() {
      return account;
    }

    public long getTenancyId() {
      return tenancyId;
    }
  }
}
