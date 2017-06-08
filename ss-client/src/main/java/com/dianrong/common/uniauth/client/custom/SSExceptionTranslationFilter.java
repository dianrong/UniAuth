package com.dianrong.common.uniauth.client.custom;

import com.dianrong.common.uniauth.common.client.ZooKeeperConfig;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.Assert;

@Slf4j
public class SSExceptionTranslationFilter extends ExceptionTranslationFilter {

  @Autowired
  private ZooKeeperConfig zooKeeperConfig;

  /**
   * 如果不注入则使用默认的实现SimpleRedirectFormat.
   */
  @Autowired(required = false)
  private CustomizedRedirectFormat customizedRedirectFormat = new SimpleRedirectFormat();

  /**
   * 当Ajax请求的时候处理其返回值.
   */
  private AjaxResponseProcessor ajaxResponseProcessor;

  public void setAjaxResponseProcessor(AjaxResponseProcessor ajaxResponseProcessor) {
    Assert.notNull(ajaxResponseProcessor);
    this.ajaxResponseProcessor = ajaxResponseProcessor;
  }

  public void setZooKeeperConfig(ZooKeeperConfig zooKeeperConfig) {
    Assert.notNull(zooKeeperConfig);
    this.zooKeeperConfig = zooKeeperConfig;
  }

  public void setCustomizedRedirectFormat(CustomizedRedirectFormat customizedRedirectFormat) {
    this.customizedRedirectFormat = customizedRedirectFormat;
  }

  public SSExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint,
      RequestCache requestCache) {
    super(authenticationEntryPoint, requestCache);
  }

  public SSExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
    super(authenticationEntryPoint);
  }

  @Override
  public void afterPropertiesSet() {
    if (ajaxResponseProcessor == null) {
      ajaxResponseProcessor = new UniauthAjaxResponseProcessor(zooKeeperConfig);
    }
  }

  protected void sendStartAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, AuthenticationException reason)
      throws ServletException, IOException {
    // SEC-112: Clear the SecurityContextHolder's Authentication, as the
    // existing Authentication is no longer considered valid
    SecurityContextHolder.getContext().setAuthentication(null);
    if (HttpRequestUtil.isAjaxRequest(request) || HttpRequestUtil.isCorsRequest(request)) {
      log.debug("This ia an ajax or cors request, return json to client side.");
      this.ajaxResponseProcessor.sendAjaxResponse(request, response, customizedRedirectFormat);
    } else {
      super.sendStartAuthentication(request, response, chain, reason);
    }
  }
}
