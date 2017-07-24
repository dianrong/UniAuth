package com.dianrong.common.uniauth.cas.controller;

import com.dianrong.common.uniauth.cas.exp.FreshUserException;
import com.dianrong.common.uniauth.cas.exp.MultiUsersFoundException;
import com.dianrong.common.uniauth.cas.exp.UserPasswordNotMatchException;
import com.dianrong.common.uniauth.cas.model.CasGetServiceTicketModel;
import com.dianrong.common.uniauth.cas.model.CasLoginCaptchaInfoModel;
import com.dianrong.common.uniauth.cas.model.CasRememberMeUsernamePasswordCredential;
import com.dianrong.common.uniauth.cas.service.CfgService;
import com.dianrong.common.uniauth.cas.util.WebScopeUtil;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.util.JsonUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AccountDisabledException;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketCreationException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.util.UniqueTicketIdGenerator;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 用于获取登陆使用的service ticket处理的controller.
 * 
 * @author wanglin
 */

@Deprecated
@Slf4j
@Controller
@RequestMapping("/serviceticket")
public class GetServiceTicketController {

  @Autowired
  private List<ArgumentExtractor> argumentExtractors;

  @Autowired
  private CookieRetrievingCookieGenerator warnCookieGenerator;

  @Autowired
  private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

  @Autowired
  private CentralAuthenticationService centralAuthenticationService;

  @Autowired
  private TicketRegistry ticketRegistry;

  @Autowired
  @Qualifier("loginTicketUniqueIdGenerator")
  private UniqueTicketIdGenerator ticketIdGenerator;

  @Autowired
  private CfgService cfgService;

  /**
   * 初始化cookie的位置.
   */
  private boolean pathPopulated = false;

  /**
   * 登陆成功的获取ST的接口.
   */
  @RequestMapping(value = "/query", method = RequestMethod.GET)
  public void queryServiceTicket(HttpServletRequest request, HttpServletResponse response) {
    try {
      final String tgtId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
      // tgtId is not exist
      if (StringUtils.isEmpty(tgtId)) {
        sendResponse(response, new CasGetServiceTicketModel(false, "relogin"));
        return;
      }
      final Ticket ticket = this.ticketRegistry.getTicket(tgtId);
      if (ticket == null || ticket.isExpired()) {
        sendResponse(response, new CasGetServiceTicketModel(false, "relogin"));
        return;
      }

      final Service service = WebUtils.getService(this.argumentExtractors, request);
      final ServiceTicket serviceTicket =
          this.centralAuthenticationService.grantServiceTicket(tgtId, service);
      if (StringUtils.isEmpty(serviceTicket)) {
        sendResponse(response,
            new CasGetServiceTicketModel(false, "generate service ticket failed"));
        return;
      }
      sendResponse(response, new CasGetServiceTicketModel(true, serviceTicket.getId()));
    } catch (Exception ex) {
      try {
        sendResponse(response,
            new CasGetServiceTicketModel(false, "generate service ticket failed"));
      } catch (IOException e) {
        log.warn("sendResponse failed!", e);
      }
      log.warn("failed to query service ticket", ex);
    }
  }

  /**
   * 获取登陆的Ticket,前端处理访问为iframe+window.name.
   */
  @RequestMapping(value = "/customlogin", method = RequestMethod.POST)
  public void iframeLogin(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    setValueToJsWindowNameResponse(response, loginProcess(request, response));
  }

  /**
   * 普通的API方式返回JSON字符串.
   */
  @RequestMapping(value = "/api/login", method = RequestMethod.POST)
  public void apilogin(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    sendResponse(response, loginProcess(request, response));
  }

  /**
   * 登陆业务逻辑处理.
   */
  private CasGetServiceTicketModel loginProcess(HttpServletRequest request,
      HttpServletResponse response) {
    // cookie 存储路径重复设置是没关系的 因为都是设置的一样的
    if (!this.pathPopulated) {
      final String contextPath = request.getContextPath();
      final String cookiePath = StringUtils.hasText(contextPath) ? contextPath + '/' : "/";
      log.info("Setting path for cookies to: {} ", cookiePath);
      this.warnCookieGenerator.setCookiePath(cookiePath);
      this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
      this.pathPopulated = true;
    }
    try {
      // 校验
      CasGetServiceTicketModel validResult = beforeLoginValidation(request, response);
      if (validResult != null) {
        return captchaValidationProcess(request, response, validResult);
      }
      CasRememberMeUsernamePasswordCredential credentials = createCredential(request);
      // call AuthenticationHandlers
      TicketGrantingTicket ticketGrantingTicketId =
          this.centralAuthenticationService.createTicketGrantingTicket(credentials);

      // create service ticket
      final Service service = WebUtils.getService(this.argumentExtractors, request);

      // 获取service 参数失败
      if (service == null) {
        return captchaValidationProcess(request, response,
            new CasGetServiceTicketModel(false, "service parameter is invalid"));
      }
      final ServiceTicket serviceTicket = this.centralAuthenticationService
          .grantServiceTicket(ticketGrantingTicketId.getId(), service);

      // 删除原来的tgt
      this.ticketGrantingTicketCookieGenerator.removeCookie(response);
      // 写入tgt
      this.ticketGrantingTicketCookieGenerator.addCookie(request, response,
          ticketGrantingTicketId.getId());
      // this.warnCookieGenerator.addCookie(request, response, "true");
      // 返回处理结果
      return captchaValidationProcess(request, response,
          new CasGetServiceTicketModel(true, serviceTicket.getId()));
    } catch (Exception e) {
      log.error("login failed", e);
      return captchaValidationProcess(request, response, getExceptionInner(e));
    }
  }

  /**
   * 登陆之前的校验处理.
   * @return null:validation success,
   *                     non null:validation failed
   */
  private CasGetServiceTicketModel beforeLoginValidation(HttpServletRequest request,
      HttpServletResponse response) {
    // 校验验证码
    CasLoginCaptchaInfoModel captchaInfo = WebScopeUtil.getValFromSession(request.getSession(),
        AppConstants.CAS_USER_LOGIN_CAPTCHA_VALIDATION_SESSION_KEY);
    if (captchaInfo == null) {
      WebScopeUtil.putCaptchaInfoToSession(request.getSession(), new CasLoginCaptchaInfoModel());
    } else {
      if (!captchaInfo.canLoginWithoutCaptcha()) {
        // 校验验证码
        String serverCaptcha = WebScopeUtil.getCaptchaFromSession(request.getSession());
        String clientCaptha = request.getParameter("captcha");
        if (serverCaptcha == null || !serverCaptcha.equals(clientCaptha)) {
          return new CasGetServiceTicketModel(false,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_CAPTCHA_VALID_FAILED,
              "valid parameter captcha failed");
        }
      }
    }
    // 验证lt
    String lt = getCustomLoginLoginTicketAndRemove(request);
    if (lt == null || !lt.equals(request.getParameter("lt"))) {
      return new CasGetServiceTicketModel(false,
          CasGetServiceTicketModel.LOGIN_EXCEPTION_LT_VALID_FAILED, "valid parameter lt failed");
    }
    return null;
  }

  /**
   * 定义异常与异常编码的关联关系.
   */
  private static final Map<Class<? extends Exception>, String> exceptionCodeMap =
      new HashMap<Class<? extends Exception>, String>() {
        private static final long serialVersionUID = 296454014783954623L;
        {
          put(AccountNotFoundException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_USER_NOT_FOUND);
          put(UserPasswordNotMatchException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_USER_NAME_PSWD_NOT_MATCH);
          put(MultiUsersFoundException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_MUTILE_USER_FOUND);
          put(AccountDisabledException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_USER_DISABLED);
          put(AccountLockedException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_TOO_MANY_FAILED);
          put(FreshUserException.class, CasGetServiceTicketModel.LOGIN_EXCEPTION_NEED_INIT_PSWD);
          put(CredentialExpiredException.class,
              CasGetServiceTicketModel.LOGIN_EXCEPTION_NEED_UPDATE_PSWD);
          put(FailedLoginException.class, CasGetServiceTicketModel.LOGIN_EXCEPTION_LOGINFAILED);
        }
      };

  private CasGetServiceTicketModel getExceptionInner(Exception e) {
    if (e instanceof AuthenticationException) {
      AuthenticationException aexception = (AuthenticationException) e;
      Map<String, Class<? extends Exception>> exmap = aexception.getHandlerErrors();
      if (exmap != null) {
        for (Class<? extends Exception> loginException : exmap.values()) {
          String errorCode = exceptionCodeMap.get(loginException);
          if (errorCode != null) {
            return new CasGetServiceTicketModel(false, errorCode, e.getMessage());
          }
        }
      }
      return new CasGetServiceTicketModel(false, CasGetServiceTicketModel.LOGIN_EXCEPTION_UNKNOW,
          e.getMessage());
    } else if (e instanceof TicketCreationException) {
      return new CasGetServiceTicketModel(false,
          CasGetServiceTicketModel.LOGIN_EXCEPTION_CREATE_SERVICE_FAILED, e.getMessage());
    } else {
      // unknow exception
      return new CasGetServiceTicketModel(false, CasGetServiceTicketModel.LOGIN_EXCEPTION_UNKNOW,
          e.getMessage());
    }
  }

  /**
   * 获取登陆对象.
   */
  private CasRememberMeUsernamePasswordCredential createCredential(HttpServletRequest request) {
    String username = request.getParameter("identity");
    String password = request.getParameter("password");
    boolean rememberMe = Boolean.valueOf(request.getParameter("rememberMe"));
    String tenancyCode = request.getParameter("tenancyCode");
    CasRememberMeUsernamePasswordCredential credential =
        new CasRememberMeUsernamePasswordCredential();
    credential.setUsername(username);
    credential.setPassword(password);
    credential.setRememberMe(rememberMe);
    credential
        .setTenancyCode(tenancyCode == null ? AppConstants.DEFAULT_TANANCY_CODE : tenancyCode);
    return credential;
  }

  /**
   * 获取业务系统自定义登陆的的.
   */
  private String getCustomLoginLoginTicketAndRemove(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      String lt = (String) (session.getAttribute(AppConstants.CAS_CUSTOM_LOGIN_LT_KEY));
      session.removeAttribute(AppConstants.CAS_CUSTOM_LOGIN_LT_KEY);
      return lt;
    }
    return null;
  }

  /**
   * 替换Session中的Login Ticket.
   *
   * @param newLt new login ticket
   */
  private void replaceLoginTicket(HttpServletRequest request, String newLt) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.setAttribute(AppConstants.CAS_CUSTOM_LOGIN_LT_KEY, newLt);
    }
  }

  /**
   * 对发送结果进行处理.
   */
  private CasGetServiceTicketModel captchaValidationProcess(HttpServletRequest request,
      HttpServletResponse response, CasGetServiceTicketModel obj) {
    // this obj can not be null
    CasLoginCaptchaInfoModel captchaInfo =
        WebScopeUtil.getCaptchaInfoFromSession(request.getSession());
    if (captchaInfo == null) {
      captchaInfo = new CasLoginCaptchaInfoModel();
      WebScopeUtil.putCaptchaInfoToSession(request.getSession(), captchaInfo);
    }
    // 进行处理
    if (!obj.getResultSuccess()) {
      // 重新生成lt
      final String loginTicket =
          this.ticketIdGenerator.getNewTicketId(AppConstants.CAS_LOGIN_TICKET_PREFIX);
      replaceLoginTicket(request, loginTicket);
      obj.setLt(loginTicket);

      // 判断是否需要验证码
      if (!captchaInfo.canLoginWithouCaptchaForFailedOnce()) {
        obj.setCaptchapath(cfgService.getCaptchaAbsolutePath());
      }
    } else {
      // remove session lt
      getCustomLoginLoginTicketAndRemove(request);
      // 登陆成功 验证码错误次数归0
      captchaInfo.reInit();
    }
    return obj;
  }

  /**
   * 将返回的值放到js的window对象中，主要是为处理iframe跨域传值的问题.
   */
  private void setValueToJsWindowNameResponse(HttpServletResponse response,
      CasGetServiceTicketModel info) throws IOException {
    // 设置返回的mime类型
    response.setContentType("text/html;charset=utf-8");
    sendResponse(response, "<script>window.name='" + JsonUtil.object2Jason(info) + "';</script>");
  }

  /**
   * 发送结果到response输出流.
   */
  private void sendResponse(HttpServletResponse response, Object obj) throws IOException {
    if (obj == null) {
      response.getWriter().write("");
    } else if (obj instanceof String) {
      response.getWriter().write((String) obj);
    } else {
      response.getWriter().write(JsonUtil.object2Jason(obj));
    }
  }
}
