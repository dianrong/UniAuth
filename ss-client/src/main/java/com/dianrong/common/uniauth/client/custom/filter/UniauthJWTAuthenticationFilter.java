package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.jwt.ComposedJWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTAuthenticationRequestMatcher;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil.JWTUserTagInfo;
import com.dianrong.common.uniauth.client.custom.jwt.exp.JWTInvalidAuthenticationException;
import com.dianrong.common.uniauth.client.custom.jwt.UniauthIdentityToken;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.jwt.UniauthJWTSecurity;
import com.dianrong.common.uniauth.common.jwt.UniauthUserJWTInfo;
import com.dianrong.common.uniauth.common.jwt.exp.InvalidJWTExpiredException;
import com.dianrong.common.uniauth.common.jwt.exp.LoginJWTExpiredException;
import com.dianrong.common.uniauth.common.util.Assert;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 用于JWT的身份认证实现.
 * 
 * @author wanglin
 *
 */
@Slf4j
public class UniauthJWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter
    implements UniauthAuthenticationFilter {

  /**
   * JWT验证工具.
   */
  private final UniauthJWTSecurity uniauthJWTSecurity;

  /**
   * 获取JWT工具类, 默认值.
   */
  private JWTQuery jwtQuery = new ComposedJWTQuery();

  /**
   * 拦截登陆的请求.
   */
  private RequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/login/cas");

  /**
   * 覆盖父类中的AuthenticationSuccessHandler.
   */
  private AuthenticationSuccessHandler localSuccessHandler;

  /**
   * 请求身份拦截匹配.
   */
  private RequestMatcher requiresAuthenticationRequestMatcher;

  /**
   * 定义一个什么都不做的AuthenticationSuccessHandler来替换父类中的AuthenticationSuccessHandler.
   * 
   */
  private static final class EmptyAuthenticationSuccessHandler
      implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {
      // do nothing
    }
  }

  /**
   * 用于缓存解析的JWT信息.
   */
  private static final ThreadLocal<JWTUserTagInfo> TAG_INFO_CACHE =
      new ThreadLocal<JWTUserTagInfo>();

  public UniauthJWTAuthenticationFilter(UniauthJWTSecurity uniauthJWTSecurity, JWTQuery jwtQuery) {
    this(new JWTAuthenticationRequestMatcher(jwtQuery), uniauthJWTSecurity);
    Assert.notNull(jwtQuery);
    this.jwtQuery = jwtQuery;
  }

  public UniauthJWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
      UniauthJWTSecurity uniauthJWTSecurity) {
    super(requiresAuthenticationRequestMatcher);
    Assert.notNull(requiresAuthenticationRequestMatcher);
    Assert.notNull(uniauthJWTSecurity);
    this.requiresAuthenticationRequestMatcher = requiresAuthenticationRequestMatcher;
    this.uniauthJWTSecurity = uniauthJWTSecurity;
    this.localSuccessHandler = super.getSuccessHandler();
    super.setAuthenticationSuccessHandler(new EmptyAuthenticationSuccessHandler());
  }

  @Override
  public AuthenticationType authenticationType() {
    return AuthenticationType.JWT;
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

  public JWTQuery getJwtQuery() {
    return jwtQuery;
  }

  public void setJwtQuery(JWTQuery jwtQuery) {
    Assert.notNull(jwtQuery);
    this.jwtQuery = jwtQuery;
    if (this.requiresAuthenticationRequestMatcher instanceof JWTAuthenticationRequestMatcher) {
      ((JWTAuthenticationRequestMatcher) this.requiresAuthenticationRequestMatcher)
          .setJwtQuery(jwtQuery);
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
    return this.requiresAuthenticationRequestMatcher.matches(request);
  }

  /**
   * 设置登陆的请求的URL.
   */
  public void setLoginRequestUrl(String loginRequestUrl) {
    setLoginRequestRequestMatcher(new AntPathRequestMatcher(loginRequestUrl));
  }

  @Override
  public int getOrder() {
    return -100;
  }
}
