package com.dianrong.common.uniauth.client.custom.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeUnit;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.client.custom.basicauth.UniauthBasicAuthToken;
import com.dianrong.common.uniauth.client.custom.handler.BasicAuthAuthenticationFailureHandler;
import com.dianrong.common.uniauth.client.custom.handler.EmptyAuthenticationSuccessHandler;
import com.dianrong.common.uniauth.client.custom.model.ItemBox;
import com.dianrong.common.uniauth.client.custom.model.StatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.common.cache.UniauthCache;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.exp.UniauthCommonException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.HttpRequestUtil;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于BasicAuth的实现.
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
   * 缓存的名称.
   */
  private static final String BASIC_AUTH_CACHE_NAME = "BASIC_AUTH_CACHE";

  private UniauthCacheManager uniauthCacheManager;

  /**
   * 缓存的分钟数.
   */
  private long cacheMinutes = 10;

  /**
   * 覆盖父类中的AuthenticationSuccessHandler.
   */
  private AuthenticationSuccessHandler loginSuccessHandler;

  public UniauthBasicAuthAuthenticationFilter(UniauthCacheManager uniauthCacheManager) {
    super(new AntPathRequestMatcher("/**"));
    Assert.notNull(uniauthCacheManager, "UniauthCacheManager can not be null");
    this.uniauthCacheManager = uniauthCacheManager;
    super.setAuthenticationSuccessHandler(new EmptyAuthenticationSuccessHandler());
    super.setAuthenticationFailureHandler(new BasicAuthAuthenticationFailureHandler());
  }

  @Override
  public AuthenticationType authenticationType() {
    // 默认为始终启动状态.
    return AuthenticationType.ALL;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    try {
      super.doFilter(req, res, chain);
    } finally {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      // 清理Token信息
      if (authentication instanceof StatelessAuthenticationSuccessToken) {
        SecurityContextHolder.clearContext();
      }
      if (authentication instanceof ItemBox) {
        StatelessAuthenticationSuccessToken statelessAuthenticationSuccessToken =
            ItemBox.getItem((ItemBox) authentication, StatelessAuthenticationSuccessToken.class);
        if (statelessAuthenticationSuccessToken != null) {
          SecurityContextHolder.clearContext();
        }
      }
    }
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    BasicAuth basicAuth = null;
    try {
      basicAuth = getBasicAuthInfo(request);
    } catch (UnsupportedEncodingException e) {
      log.error("Failed get BasicAuth info from request", e);
    }
    if (basicAuth == null) {
      log.warn("attemptAuthentication, but can not get basicAuth from request.");
      throw new UniauthCommonException(
          "attemptAuthentication, but can not get basicAuth from request.");
    }
    log.debug("Attempt authentication:TenancyCode:{},Account:{}", basicAuth.tenancyCode,
        basicAuth.account);
    String requestIp = HttpRequestUtil.ipAddress(request);
    UniauthBasicAuthToken basicAuthToken = new UniauthBasicAuthToken(basicAuth.tenancyCode,
        basicAuth.account, basicAuth.password, requestIp);
    Authentication authentication = this.getAuthenticationManager().authenticate(basicAuthToken);
    innerSuccessfulAuthentication(request, response, basicAuth, authentication);
    return authentication;
  }

  /**
   * 验证成功.
   * 
   * @param basicAuth basicAuth信息,不为空.
   */
  protected void innerSuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, BasicAuth basicAuth, Authentication authResult)
      throws IOException, ServletException {
    // Cache authentication
    String md5Str = basicAuth.getMd5();
    UniauthCache uniauthCache = uniauthCacheManager.getCache(BASIC_AUTH_CACHE_NAME);
    uniauthCache.put(md5Str, authResult, cacheMinutes, TimeUnit.MINUTES);
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
      basicAuth = getBasicAuthInfo(request);
    } catch (UnsupportedEncodingException e) {
      log.error("Failed get BasicAuth info from request", e);
    }
    // 不属于BasicAuth请求.
    if (basicAuth == null) {
      return false;
    }
    String md5Str = basicAuth.getMd5();
    UniauthCache uniauthCache = uniauthCacheManager.getCache(BASIC_AUTH_CACHE_NAME);
    Authentication authentication = uniauthCache.get(md5Str, Authentication.class);
    if (authentication == null || !authentication.isAuthenticated()) {
      return true;
    }

    BasicAuthStatelessAuthenticationSuccessToken token = null;
    if (authentication instanceof BasicAuthStatelessAuthenticationSuccessToken) {
      token = (BasicAuthStatelessAuthenticationSuccessToken) authentication;
    }
    if (token == null && authentication instanceof ItemBox) {
      token = ItemBox.getItem((ItemBox) authentication, BasicAuthStatelessAuthenticationSuccessToken.class);
    }
    if (token == null) {
      log.warn("Cache conflict, authentication:" + authentication);
      return true;
    }
    // 匹配一下tenancyCode 和 account
    if (!ObjectUtil.objectEqual(token.getTenancyCode(), basicAuth.tenancyCode)
        || !ObjectUtil.objectEqual(token.getAccount(), basicAuth.account)) {
      log.warn("Basic auth cache key conflict:TenancyCode:{},Account:{}", token.getTenancyCode(),
          token.getAccount());
      // 此种方式会导致不停的访问服务验证身份,缓存不起作用.
      return true;
    }

    // set authentication to spring security holder
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return false;
  }

  /**
   * 如果不是属于BasicAuth的,直接返回空.
   */
  private BasicAuth getBasicAuthInfo(HttpServletRequest request)
      throws UnsupportedEncodingException {
    String header = request.getHeader(HEADER_NAME);
    if (header == null || !header.startsWith(HEADER_VALUE_PREFIX)) {
      return null;
    }
    byte[] base64Token = header.substring(HEADER_VALUE_PREFIX.length()).getBytes("UTF-8");
    byte[] decoded;
    try {
      decoded = Base64.decode(base64Token);
    } catch (IllegalArgumentException e) {
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
      Assert.notNull(tenancyCode, "TenancyCode can not be null");
      Assert.notNull(account, "Account can not be null");
      this.tenancyCode = tenancyCode;
      this.account = account;
      this.password = password;
    }

    public String getMd5() {
      StringBuilder sb = new StringBuilder();
      sb.append(tenancyCode).append(":").append(account).append(":").append(password);
      return StringUtil.md5(sb.toString());
    }
  }

  public long getCacheMinutes() {
    return cacheMinutes;
  }

  public void setCacheMinutes(long cacheMinutes) {
    this.cacheMinutes = cacheMinutes;
  }

  @Override
  public int getOrder() {
    return -100;
  }
}
