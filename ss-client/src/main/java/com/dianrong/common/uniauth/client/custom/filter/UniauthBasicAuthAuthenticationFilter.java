package com.dianrong.common.uniauth.client.custom.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dianrong.common.uniauth.client.custom.handler.EmptyAuthenticationSuccessHandler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.custom.jwt.*;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil.JWTUserTagInfo;
import com.dianrong.common.uniauth.client.custom.jwt.exp.JWTInvalidAuthenticationException;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.UniauthUserJWTInfo;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于Basic auth的实现.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class UniauthBasicAuthAuthenticationFilter extends AbstractAuthenticationProcessingFilter
    implements UniauthAuthenticationFilter {

  /**
   * Basic auth信息的header的名称.
   */
  public static final String HEADER_NAME = "Authorization";

  /**
   * Basic auth信息的开头值.
   */
  public static final String HEADER_VALUE_PREFIX = "Basic ";

  /**
   * 信息的分割符.
   */
  public static final String DELIMITER = ":";

  /**
   * 信息项的个数3个: 租户编码:账号:密码
   */
  public static final int VALUE_ITEM_NUM = 3;

  /**
   * 拦截登陆的请求.
   */
  private RequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/login/cas");

  /**
   * 覆盖父类中的AuthenticationSuccessHandler.
   */
  private AuthenticationSuccessHandler localSuccessHandler;

  public UniauthBasicAuthAuthenticationFilter() {
    super((String)null);
    super.setAuthenticationSuccessHandler(new EmptyAuthenticationSuccessHandler());
  }

  @Override
  public AuthenticationType authenticationType() {
    // 默认启动状态,不管怎么配置该filter一直启用.
    return AuthenticationType.ALL;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    String jwt = jwtQuery.getJWT(request);
    try {
      UniauthUserJWTInfo info = uniauthJWTSecurity.getInfoFromJwt(jwt);
      String identity = info.getIdentity();
      Long tenancyId = info.getTenancyId();

      // Cache
      JWTUserTagInfo tagCache = new JWTUserTagInfo();
      tagCache.setIdentity(identity);
      tagCache.setTenancyId(tenancyId);
      TAG_INFO_CACHE.set(tagCache);

      UniauthIdentityToken authRequest = new UniauthIdentityToken(identity, tenancyId);
      return this.getAuthenticationManager().authenticate(authRequest);
    } catch (LoginJWTExpiredException | InvalidJWTExpiredException e) {
      log.error("JWT is invalid!", e);
      throw new JWTInvalidAuthenticationException(jwt + " is a invalid JWT string!", e);
    }
  }

  /**
   * 设置成功认证身份的标识.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    // 完成登陆成功的处理流程
    super.successfulAuthentication(request, response, chain, authResult);
    JWTUserTagInfo tagInfo = TAG_INFO_CACHE.get();
    JWTWebScopeUtil.refreshJWTUserInfoTag(tagInfo, request);

    // 判断是否继续访问,还是跳转到首页
    if (loginRequestMatcher.matches(request)) {
      // 登陆操作,需要跳转到首页去
      this.localSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    } else {
      // 普通访问, 继续执行Filter链
      chain.doFilter(request, response);
    }
  }

  /**
   * 重写覆盖父类的setAuthenticationSuccessHandler方法.
   */
  @Override
  public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
    Assert.notNull(successHandler, "successHandler cannot be null");
    this.localSuccessHandler = successHandler;
  }

  @Override
  public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response)  {
    BasicAuth basicAuth = null;
    try {
      basicAuth = getBasicAuthInfo(request);
    } catch (UnsupportedEncodingException e) {
      log.error("Failed get BasicAuth info from request", e);
    }
    // 不属于BasicAuth请求.
    if (basicAuth == null) {
      return false;
    }

  }

  /**
   * 如果不是属于BasicAuth的,直接返回空.
   */
  private BasicAuth getBasicAuthInfo(HttpServletRequest request) throws UnsupportedEncodingException {
    String header = request.getHeader(HEADER_NAME);
    if (header == null || !header.startsWith(HEADER_VALUE_PREFIX)) {
      return null;
    }
    byte[] base64Token = header.substring(HEADER_VALUE_PREFIX.length()).getBytes("UTF-8");
    byte[] decoded;
    try {
      decoded = Base64.decode(base64Token);
    }
    catch (IllegalArgumentException e) {
      throw new BadCredentialsException("Failed to decode basic authentication token");
    }
    String token = new String(decoded, "UTF-8");
    int delim = token.indexOf(DELIMITER);
    if (delim == -1) {
      throw new BadCredentialsException("Invalid basic authentication token");
    }
    String tenancyCode = token.substring(0, delim);

    token = token.substring(delim + 1);
    delim = token.indexOf(DELIMITER);
    if (delim == -1) {
      throw new BadCredentialsException("Invalid basic authentication token");
    }
    String account = token.substring(0, delim);
    String password = token.substring(delim + 1);
    return new BasicAuth(tenancyCode, account, password);
  }

  /**
   * 获取的BasicAuth信息.
   */
  private static final class BasicAuth {
    private final String tenancyCode;
    private final String account;
    private final String password;
    private BasicAuth(String tenancyCode, String account, String password) {
      this.tenancyCode =tenancyCode;
      this.account = account;
      this.password = password;
    }
  }

  /**
   * 设置登陆的请求的URL.
   */
  public void setLoginRequestUrl(String loginRequestUrl) {
    setLoginReuqestRequestMatcher(new AntPathRequestMatcher(loginRequestUrl));
  }

  @Override
  public int getOrder() {
    return -100;
  }
}
