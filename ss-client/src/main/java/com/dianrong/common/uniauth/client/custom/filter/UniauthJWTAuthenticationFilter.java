package com.dianrong.common.uniauth.client.custom.filter;

import com.dianrong.common.uniauth.client.custom.jwt.JWTAuthenticationRequestMatcher;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil;
import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil.JWTUserTagInfo;
import com.dianrong.common.uniauth.client.custom.jwt.SimpleJWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.exp.JWTInvalidAuthenticationException;
import com.dianrong.common.uniauth.client.custom.model.UniauthIdentityToken;
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
   * 获取JWT工具类.
   */
  private JWTQuery jwtQuery = new SimpleJWTQuery();
  
  /**
   * 用于缓存解析的JWT信息.
   */
  private static final ThreadLocal<JWTUserTagInfo> TAG_INFO_CACHE = new ThreadLocal<JWTUserTagInfo>();

  public UniauthJWTAuthenticationFilter(UniauthJWTSecurity uniauthJWTSecurity) {
    this(new JWTAuthenticationRequestMatcher(), uniauthJWTSecurity);
  }

  public UniauthJWTAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
      UniauthJWTSecurity uniauthJWTSecurity) {
    super(requiresAuthenticationRequestMatcher);
    Assert.notNull(uniauthJWTSecurity);
    this.uniauthJWTSecurity = uniauthJWTSecurity;
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
      
      // cache
      JWTUserTagInfo tagCache = new JWTUserTagInfo();
      tagCache.setIdentity(identity);
      tagCache.setTenancyId(tenancyId);
      TAG_INFO_CACHE.set(tagCache);
      
      UniauthIdentityToken authRequest = new UniauthIdentityToken(identity, tenancyId);
      return  this.getAuthenticationManager().authenticate(authRequest);
    } catch (LoginJWTExpiredException | InvalidJWTExpiredException e) {
      log.error("JWT is invalid!", e);
      throw new JWTInvalidAuthenticationException(jwt + " is a invalid JWT string!", e);
    }
  }
  
  /**
   * 设置成功认证身份的标识.
   */
  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain, Authentication authResult)
      throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    JWTUserTagInfo tagInfo = TAG_INFO_CACHE.get();
    JWTWebScopeUtil.refreshJWTUserInfoTag(tagInfo, request);
}

  public JWTQuery getJwtQuery() {
    return jwtQuery;
  }

  public void setJwtQuery(JWTQuery jwtQuery) {
    this.jwtQuery = jwtQuery;
  }
}
