package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.client.custom.redirect.CompatibleAjaxRedirect;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 处理登出成功的handler.兼容ajax请求的登陆操作.
 */
public class CompatibleAjaxLoginSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  private RedirectStrategy compatibleAjaxRedirect = new CompatibleAjaxRedirect();
  
  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    String targetUrl = super.determineTargetUrl(request, response);
    compatibleAjaxRedirect.sendRedirect(request, response, targetUrl);
  }
}
