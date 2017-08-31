package com.dianrong.common.uniauth.client.custom.handler;

import com.dianrong.common.uniauth.client.custom.redirect.CompatibleAjaxRedirect;
import com.dianrong.common.uniauth.common.util.Assert;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.StringUtils;

/**
 * JWT身份验证失败的处理Handler.
 * 
 * 验证失败则直接跳转回到登陆页面.
 * 
 * @author wanglin
 *
 */
public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

  /**
   * 身份认证失败的跳转策略实现类.
   */
  private RedirectStrategy authenticationFailureRedirectStrategy = new CompatibleAjaxRedirect();

  private String failedAuthenticationRedirectUrl = "/";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    authenticationFailureRedirectStrategy.sendRedirect(request, response, failedAuthenticationRedirectUrl);
  }

  public String getFailedAuthenticationRedirectUrl() {
    return failedAuthenticationRedirectUrl;
  }

  public void setFailedAuthenticationRedirectUrl(String failedAuthenticationRedirectUrl) {
    if (StringUtils.hasText(failedAuthenticationRedirectUrl)) {
      this.failedAuthenticationRedirectUrl = failedAuthenticationRedirectUrl;
    } else {
      this.failedAuthenticationRedirectUrl = "/";
    }
  }

  public RedirectStrategy getAuthenticationFailureRedirectStrategy() {
    return authenticationFailureRedirectStrategy;
  }

  public void setAuthenticationFailureRedirectStrategy(
      RedirectStrategy authenticationFailureRedirectStrategy) {
    Assert.notNull(authenticationFailureRedirectStrategy);
    this.authenticationFailureRedirectStrategy = authenticationFailureRedirectStrategy;
  }
}
