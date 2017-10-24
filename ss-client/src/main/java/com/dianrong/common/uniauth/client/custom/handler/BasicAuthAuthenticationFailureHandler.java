package com.dianrong.common.uniauth.client.custom.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianrong.common.uniauth.common.util.Assert;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * BasicAuth验证失败处理handler.
 */
public class BasicAuthAuthenticationFailureHandler implements AuthenticationFailureHandler {

  public String getRealmName() {
    return realmName;
  }

  public void setRealmName(String realmName) {
    Assert.notNull(realmName, "realmName can not be null");
    this.realmName = realmName;
  }

  private String realmName;

  public BasicAuthAuthenticationFailureHandler() {
    this("realm");
  }

  public BasicAuthAuthenticationFailureHandler(String realmName) {
    Assert.notNull(realmName, "realmName can not be null");
    this.realmName = realmName;
  }

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
  }
}
