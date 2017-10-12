package com.dianrong.common.uniauth.client.custom.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.custom.handler.EmptyAuthenticationSuccessHandler;
import com.dianrong.common.uniauth.client.custom.jwt.ComposedJWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.client.custom.jwt.UniauthIdentityToken;
import com.dianrong.common.uniauth.client.custom.jwt.exp.JWTInvalidAuthenticationException;
import com.dianrong.common.uniauth.client.custom.model.ItemBox;
import com.dianrong.common.uniauth.client.custom.model.StatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.common.cache.UniauthCache;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.UniauthUserJWTInfo;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.ObjectUtil;
import com.dianrong.common.uniauth.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 用于JWT的身份认证实现.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class UniauthJWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter
    implements UniauthAuthenticationFilter {
  private static final String JWT_CACHE_NAME = "JWT_CACHE";

  /**
   * JWT验证工具.
   */
  private final UniauthJWTSecurity uniauthJWTSecurity;

  /**
   * 获取JWT工具类, 默认值.
   */
  private JWTQuery jwtQuery = new ComposedJWTQuery();

  private UniauthCacheManager uniauthCacheManager;

  /**
   * 拦截登陆的请求.
   */
  private RequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/login/cas");

  /**
   * 缓存的分钟数.
   */
  private long cacheMinutes = 10;

  /**
   * 覆盖父类中的AuthenticationSuccessHandler.
   */
  private AuthenticationSuccessHandler loginSuccessHandler;

  public UniauthJWTAuthenticationFilter(UniauthJWTSecurity uniauthJWTSecurity, JWTQuery jwtQuery,
      UniauthCacheManager uniauthCacheManager) {
    this(uniauthJWTSecurity, uniauthCacheManager);
    Assert.notNull(jwtQuery);
    this.jwtQuery = jwtQuery;
  }

  public UniauthJWTAuthenticationFilter(UniauthJWTSecurity uniauthJWTSecurity,
      UniauthCacheManager uniauthCacheManager) {
    super(new AntPathRequestMatcher("/**"));
    Assert.notNull(uniauthJWTSecurity);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
    this.loginSuccessHandler = super.getSuccessHandler();
    this.uniauthCacheManager = uniauthCacheManager;
    super.setAuthenticationSuccessHandler(new EmptyAuthenticationSuccessHandler());
  }

  @Override
  public AuthenticationType authenticationType() {
    return AuthenticationType.JWT;
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
    String jwt = jwtQuery.getJWT(request);
    try {
      UniauthUserJWTInfo info = uniauthJWTSecurity.getInfoFromJwt(jwt);
      String identity = info.getIdentity();
      Long tenancyId = info.getTenancyId();
      UniauthIdentityToken authRequest = new UniauthIdentityToken(identity, tenancyId);
      Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

      // Cache authentication
      String md5Str = StringUtil.md5(jwt);
      UniauthCache uniauthCache = uniauthCacheManager.getCache(JWT_CACHE_NAME);
      uniauthCache.put(md5Str, authentication, cacheMinutes, TimeUnit.MINUTES);

      return authentication;
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
    // 判断是否继续访问,还是跳转到首页
    if (loginRequestMatcher.matches(request)) {
      // 登陆操作,需要跳转到首页去
      this.loginSuccessHandler.onAuthenticationSuccess(request, response, authResult);
    } else {
      // 普通访问, 继续执行Filter链
      chain.doFilter(request, response);
    }
  }

  public JWTQuery getJwtQuery() {
    return jwtQuery;
  }

  public void setJwtQuery(JWTQuery jwtQuery) {
    Assert.notNull(jwtQuery, "JWTQuery can not be null ");
    this.jwtQuery = jwtQuery;
  }

  /**
   * 重写覆盖父类的setAuthenticationSuccessHandler方法.
   */
  @Override
  public void setAuthenticationSuccessHandler(AuthenticationSuccessHandler successHandler) {
    Assert.notNull(successHandler, "successHandler cannot be null");
    this.loginSuccessHandler = successHandler;
  }

  /**
   * 设置登陆的请求匹配的Matcher.
   * 
   * @param requestMatcher
   */
  public final void setLoginRequestRequestMatcher(RequestMatcher requestMatcher) {
    Assert.notNull(requestMatcher, "requestMatcher cannot be null");
    this.loginRequestMatcher = requestMatcher;
  }

  @Override
  public boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
    String jwt = jwtQuery.getJWT(request);
    if (jwt == null) {
      return false;
    }

    // 验证JWT.
    UniauthUserJWTInfo info;
    try {
      info = uniauthJWTSecurity.getInfoFromJwt(jwt);
    } catch (LoginJWTExpiredException | InvalidJWTExpiredException e) {
      log.error("JWT is invalid!", e);
      throw new JWTInvalidAuthenticationException(jwt + " is a invalid JWT string!", e);
    }
    String key = StringUtil.md5(jwt);
    UniauthCache uniauthCache = uniauthCacheManager.getCache(JWT_CACHE_NAME);
    Authentication authentication =
        uniauthCache.get(key, Authentication.class);
    if (authentication == null || !authentication.isAuthenticated()) {
      return true;
    }

    JWTStatelessAuthenticationSuccessToken token = null;
    if (authentication instanceof JWTStatelessAuthenticationSuccessToken) {
      token = (JWTStatelessAuthenticationSuccessToken)authentication;
    }
    if (token == null && authentication instanceof ItemBox) {
      token = ItemBox.getItem((ItemBox) authentication, JWTStatelessAuthenticationSuccessToken.class);
    }
    if (token == null) {
      log.warn("Cache conflict, authentication:" + authentication);
      return true;
    }
    // 匹配一下tenancyCode 和 account
    if (!ObjectUtil.objectEqual(info.getTenancyId(), token.getTenancyId())
        || !ObjectUtil.objectEqual(info.getIdentity(), token.getIdentity())) {
      log.warn("JWT cache key conflict:TenancyId:{},Identity:{}", token.getTenancyId(),
          token.getIdentity());
      // 此种方式会导致不停的访问服务验证身份,缓存不起作用.
      return true;
    }

    // set authentication to spring security holder
    SecurityContextHolder.getContext().setAuthentication(authentication);
    return false;

  }

  /**
   * 设置登陆的请求的URL.
   */
  public void setLoginRequestUrl(String loginRequestUrl) {
    setLoginRequestRequestMatcher(new AntPathRequestMatcher(loginRequestUrl));
  }

  public long getCacheMinutes() {
    return cacheMinutes;
  }

  public void setCacheMinutes(long cacheMinutes) {
    this.cacheMinutes = cacheMinutes;
  }

  @Override
  public int getOrder() {
    return -90;
  }
}
