package com.dianrong.common.uniauth.common.client;

import com.dianrong.common.uniauth.common.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.UrlPatternMatcherStrategy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用于处理登出操作.
 * 
 * @author wanglin
 */

@Slf4j
public class CasSimpleLogoutFilter implements Filter {

  /**
   * The URL to the CAS Server login.
   */
  private String casLogout = "/logout/cas";

  private UrlPatternMatcherStrategy logoutUrlMather;

  /**
   * 登出之后的跳转地址.
   */
  private String logoutRedirectUrl = "/";


  public CasSimpleLogoutFilter() {
    this.logoutUrlMather = new RegexUrlPatternListMatcherStrategy(this.casLogout);
  }

  public final void doFilter(final ServletRequest servletRequest,
      final ServletResponse servletResponse, final FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest request = (HttpServletRequest) servletRequest;
    final HttpServletResponse response = (HttpServletResponse) servletResponse;

    if (!isLogoutRequest(request)) {
      filterChain.doFilter(request, response);
    } else {
      // destroy session
      destroySession(request);
      
      if (!response.isCommitted()) {
        log.debug("Logout successfully, redirect to:{}", this.logoutRedirectUrl);
        response.sendRedirect(this.logoutRedirectUrl);
      }
    }
  }

  /**
   * 登出 销毁session.
   */
  protected void destroySession(final HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      log.debug("Logout, destroy session");
      session.invalidate();
    }
  }

  private boolean isLogoutRequest(final HttpServletRequest request) {
    final StringBuffer urlBuffer = request.getRequestURL();
    return this.logoutUrlMather.matches(urlBuffer.toString());
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}

  @Override
  public void destroy() {}

  public String getLogoutRedirectUrl() {
    return logoutRedirectUrl;
  }

  public void setLogoutRedirectUrl(String logoutRedirectUrl) {
    Assert.notNull(logoutRedirectUrl);
    this.logoutRedirectUrl = logoutRedirectUrl;
  }

  public void setCasLogout(String casLogout) {
    Assert.notNull(casLogout);
    this.casLogout = casLogout;
    this.logoutUrlMather = new RegexUrlPatternListMatcherStrategy(this.casLogout);
  }

}
