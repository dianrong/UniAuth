package com.dianrong.common.uniauth.cas.controller.support;

import com.dianrong.common.uniauth.cas.exp.ValidateFailException;
import com.dianrong.common.uniauth.cas.util.CasConstants;
import com.dianrong.common.uniauth.common.exp.NotLoginException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.authentication.AuthenticationException;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.InvalidTicketException;
import org.jasig.cas.ticket.ServiceTicket;
import org.jasig.cas.ticket.Ticket;
import org.jasig.cas.ticket.TicketException;
import org.jasig.cas.ticket.TicketGrantingTicket;
import org.jasig.cas.ticket.registry.TicketRegistry;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 抽取CAS登陆相关操作的公用代码，其他模块采用 组合方式使用.
 *
 * @author wanglin
 */
@Slf4j
@Component
@SuppressWarnings("deprecation")
public class CasLoginSupport {

  @Autowired
  private CentralAuthenticationService centralAuthenticationService;

  @Autowired
  private CookieRetrievingCookieGenerator warnCookieGenerator;

  @Autowired
  private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

  @Resource(name = "argumentExtractors")
  private List<ArgumentExtractor> argumentExtractors;

  /**
   * Ticket registry searched for TGT by ID.
   */
  @Autowired
  private TicketRegistry ticketRegistry;

  /**
   * 标志位, 用于判断是否已经初始化过cookie path.
   */
  private volatile boolean pathPopulated;

  /** 登陆跳转相关的parameter 常量定义. **/
  /**
   * 标志位,用于判断登陆成功之后是否跳转.
   */
  private static final String SERVICE_REDIRECT_PARAMETER = "loginRedirect";

  /**
   * 进行跳转的参数值.
   */
  private static final String NEED_REDIRECT_VALUE = "true";

  /**
   * 在登陆的状态下获取tgt对象.
   *
   * @return TicketGrantingTicket 对象
   * @throws NotLoginException if not login
   */
  public TicketGrantingTicket queryTgtWithLogined(HttpServletRequest request,
      HttpServletResponse response) {
    final String tgtId = ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
    // tgtId is not exist
    if (StringUtil.strIsNullOrEmpty(tgtId)) {
      throw new NotLoginException("not login");
    }
    final Ticket ticket = ticketRegistry.getTicket(tgtId);
    if (ticket == null || ticket.isExpired()) {
      throw new NotLoginException("not login");
    }
    // 校验登陆完成 获取当前登陆账号
    return (TicketGrantingTicket) ticket;
  }

  /**
   * 生成service ticket.
   *
   * @param request HttpServletRequest
   * @param ticketGrantingTicketId 当前登陆人对应的ticket granted ticket
   * @return 新生成的service ticket
   * @throws TicketException 生成service ticket失败
   * @throws ValidateFailException 参数service不符合规范
   */
  public String grantServiceTicket(HttpServletRequest request, String ticketGrantingTicketId)
      throws TicketException, ValidateFailException {
    Service service = queryService(request);
    if (service == null) {
      throw new ValidateFailException("parameter service can not be null");
    }
    ServiceTicket serviceTicket =
        this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service);
    return serviceTicket.getId();
  }

  /**
   * 获取当前登陆用户的信息.
   *
   * @param ticketGrantingTicketId tgt
   * @return Principal
   * @throws NotLoginException if not login
   */
  public Principal getAuthenticationPrincipal(final String ticketGrantingTicketId) {
    try {
      final TicketGrantingTicket ticketGrantingTicket = this.centralAuthenticationService
          .getTicket(ticketGrantingTicketId, TicketGrantingTicket.class);
      return ticketGrantingTicket.getAuthentication().getPrincipal();
    } catch (final InvalidTicketException e) {
      log.warn(e.getMessage());
      throw new NotLoginException("not login");
    }
  }

  /**
   * 通过账号密码登陆,并返回生成的tgt.
   *
   * @param credential 登陆的credential
   * @param warnCookie warnCookie值
   * @return 登陆成功, 返回生成的tgt
   * @throws AuthenticationException 登陆失败了
   * @throws TicketException tgt创建失败
   */
  public String loginAndQueryTgt(Credential credential, boolean warnCookie,
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, TicketException {
    if (!pathPopulated) {
      final String contextPath = request.getContextPath();
      final String cookiePath = !StringUtil.strIsNullOrEmpty(contextPath) ? contextPath + "/" : "/";
      // always use default cookie path of "/"
      log.info("setting Cookie path to: {} ", cookiePath);
      if (!cookiePath.equals(warnCookieGenerator.getCookiePath())) {
        this.warnCookieGenerator.setCookiePath(cookiePath);
        this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
      }
      this.pathPopulated = true;
    }

    TicketGrantingTicket ticketGrantingTicket =
        this.centralAuthenticationService.createTicketGrantingTicket(credential);
    String ticketGrantingTicketId = ticketGrantingTicket.getId();

    // remove tgt
    this.ticketGrantingTicketCookieGenerator.removeCookie(response);
    // set new tgt cookie
    this.ticketGrantingTicketCookieGenerator.addCookie(request, response, ticketGrantingTicketId);
    // set warn cookie
    this.warnCookieGenerator.addCookie(request, response, String.valueOf(warnCookie));

    return ticketGrantingTicketId;
  }

  /**
   * 登出操作.
   */
  public void casLoginOut(HttpServletRequest request, HttpServletResponse response) {
    // if we have tgt and it is not empty, then destroy tgt first .
    final String tgtId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
    if (!StringUtil.strIsNullOrEmpty(tgtId)) {
      centralAuthenticationService.destroyTicketGrantingTicket(tgtId);
    }
    // remove tgt
    this.ticketGrantingTicketCookieGenerator.removeCookie(response);
    // remove warn cookie
    this.warnCookieGenerator.removeCookie(response);
  }

  /**
   * 从HttpServletRequest中获取service.
   *
   * @param request HttpServletRequest
   * @return 如果请求中包含service参数, 则返回对应的service对象. 不然就返回null
   */
  public Service queryService(HttpServletRequest request) {
    return WebUtils.getService(this.argumentExtractors, request);
  }

  /**
   * 根据credential登陆,并生成tgt和st,最后返回st.
   *
   * @param credential 登陆凭证(账号,密码等)
   * @param warnCookie warnCookie的值
   * @return 登陆成功生成的service ticket的值
   * @throws TicketException 生成service ticket 或者 ticket granted ticket失败
   * @throws ValidateFailException HttpServletRequest的service参数为空或者不符合规范
   * @throws AuthenticationException 登陆失败了
   */
  public String loginAndQueryTicket(Credential credential, boolean warnCookie,
      HttpServletRequest request, HttpServletResponse response)
      throws TicketException, ValidateFailException, AuthenticationException {
    Assert.notNull(credential, "cas login need credential");
    String ticketGrantingTicketId = loginAndQueryTgt(credential, warnCookie, request, response);
    Service service = queryService(request);
    if (service == null) {
      throw new ValidateFailException("parameter service can not be null");
    }
    return this.centralAuthenticationService.grantServiceTicket(ticketGrantingTicketId, service)
        .getId();
  }

  /**
   * 判断某个登陆成功的请求是否需要进行跳转(http code 302).并且检测 判断依据为参数主动指定
   *
   * @return true or false
   */
  public boolean isLoginRedirect(HttpServletRequest request) {
    boolean redirect =
        NEED_REDIRECT_VALUE.equalsIgnoreCase(request.getParameter(SERVICE_REDIRECT_PARAMETER));
    if (redirect) {
      Service service = queryService(request);
      if (service != null) {
        return true;
      }
      log.warn(
          "request's parameter {} value is {}, but parameter service is null, "
          + "please check request parameter",
          SERVICE_REDIRECT_PARAMETER, NEED_REDIRECT_VALUE);
    }
    return false;
  }

  /**
   * 判断当前登陆是否需要跳转,并且主动跳转
   *
   * @param serviceTicket 登陆成功生成的service ticket. 如果该参数为空,则不进行跳转
   * @return 是否成功进行跳转
   * @throws IOException 跳转失败
   */
  public boolean loginRedirect(HttpServletRequest request, HttpServletResponse response,
      String serviceTicket) throws IOException {
    if (StringUtil.strIsNullOrEmpty(serviceTicket)) {
      return false;
    }
    if (!isLoginRedirect(request)) {
      return false;
    }
    StringBuilder url = new StringBuilder(queryService(request).getId());
    if (url.toString().indexOf('?') == -1) {
      url.append("?");
    } else {
      url.append("&");
    }
    url.append("ticket=").append(serviceTicket);
    String targetUrl = request.getParameter(CasConstants.TARGET_URL_PARAMETER);
    if (!StringUtil.strIsNullOrEmpty(targetUrl)) {
      url.append("&").append(CasConstants.TARGET_URL_PARAMETER).append("=")
          .append(targetUrl.trim());
    }
    log.info("login success, redirect to {} ", url);
    response.sendRedirect(url.toString());
    return true;
  }
}
