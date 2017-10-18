package com.dianrong.common.uniauth.client.custom.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuth;
import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthDetector;
import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.client.custom.basicauth.UniauthBasicAuthToken;
import com.dianrong.common.uniauth.client.custom.handler.BasicAuthAuthenticationFailureHandler;
import com.dianrong.common.uniauth.client.custom.handler.EmptyAuthenticationSuccessHandler;
import com.dianrong.common.uniauth.client.custom.model.ItemBox;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.ObjectUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于BasicAuth的实现.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class UniauthBasicAuthAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
  /**
   * 控制参数,BasicAuth Filter是否启用.
   */
  private boolean enable = true;

  /**
   * 覆盖父类中的AuthenticationSuccessHandler.
   */
  private AuthenticationSuccessHandler loginSuccessHandler;

  public UniauthBasicAuthAuthenticationFilter() {
    super(new AntPathRequestMatcher("/**"));
    super.setAuthenticationSuccessHandler(new EmptyAuthenticationSuccessHandler());
    super.setAuthenticationFailureHandler(new BasicAuthAuthenticationFailureHandler());
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    if (enable) {
      super.doFilter(req, res, chain);
    } else {
      chain.doFilter(req, res);
    }
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    BasicAuth basicAuth = null;
    try {
      basicAuth = BasicAuthDetector.getBasicAuthInfo(request);
    } catch (UnsupportedEncodingException e) {
      log.error("Failed get BasicAuth info from request", e);
    }
    if (basicAuth == null) {
      log.warn("attemptAuthentication, but can not get basicAuth from request.");
      throw new UniauthCommonException(
          "attemptAuthentication, but can not get basicAuth from request.");
    }
    log.debug("Attempt authentication:TenancyCode:{},Account:{}", basicAuth.getTenancyCode(),
        basicAuth.getAccount());
    String requestIp = HttpRequestUtil.ipAddress(request);
    UniauthBasicAuthToken basicAuthToken = new UniauthBasicAuthToken(basicAuth.getTenancyCode(),
        basicAuth.getAccount(), basicAuth.getPassword(), requestIp);
    return this.getAuthenticationManager().authenticate(basicAuthToken);
  }


  /**
   * 设置成功认证身份的标识.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // 完成登陆成功的处理流程
    super.successfulAuthentication(request, response, chain, authResult);
    // 普通访问, 继续执行Filter链
    chain.doFilter(request, response);
  }

  /**
   * 重写覆盖父类的setAuthenticationSuccessHandler方法.
   */
  @Override
  public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
    Assert.notNull(successHandler, "successHandler cannot be null");
    this.loginSuccessHandler = successHandler;
  }

  @Override
  public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    BasicAuth basicAuth = null;
    try {
      basicAuth = BasicAuthDetector.getBasicAuthInfo(request);
    } catch (UnsupportedEncodingException e) {
      log.error("Failed get BasicAuth info from request", e);
    }
    // 不属于BasicAuth请求.
    if (basicAuth == null) {
      return false;
    }
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return true;
    }
    BasicAuthStatelessAuthenticationSuccessToken token = null;
    if (authentication instanceof BasicAuthStatelessAuthenticationSuccessToken) {
      token = (BasicAuthStatelessAuthenticationSuccessToken) authentication;
    }
    if (token == null && authentication instanceof ItemBox) {
      token = ItemBox.getItem((ItemBox) authentication,
          BasicAuthStatelessAuthenticationSuccessToken.class);
    }
    if (token == null) {
      log.warn("Cache conflict, authentication:" + authentication);
      return true;
    }
    // 匹配一下tenancyCode 和 account
    if (!ObjectUtil.objectEqual(token.getTenancyCode(), basicAuth.getTenancyCode())
        || !ObjectUtil.objectEqual(token.getAccount(), basicAuth.getAccount())) {
      log.warn("Basic auth cache key conflict:TenancyCode:{},Account:{}", token.getTenancyCode(),
          token.getAccount());
      // 此种方式会导致不停的访问服务验证身份,缓存不起作用.
      return true;
    }
    return false;
  }

  protected boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
  }
}
