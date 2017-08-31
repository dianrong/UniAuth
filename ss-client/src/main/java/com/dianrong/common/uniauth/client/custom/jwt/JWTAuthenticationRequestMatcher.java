package com.dianrong.common.uniauth.client.custom.jwt;

import com.dianrong.common.uniauth.client.custom.jwt.JWTWebScopeUtil.JWTUserTagInfo;
import com.dianrong.common.uniauth.common.util.Assert;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 自定义实现JWT请求需要认证身份的请求Matcher.
 * 
 * @author wanglin
 *
 */
public class JWTAuthenticationRequestMatcher implements RequestMatcher {
  /**
   * 获取JWT工具类.
   */
  private JWTQuery jwtQuery = new ComposedJWTQuery();

  public JWTQuery getJwtQuery() {
    return jwtQuery;
  }

  public void setJwtQuery(JWTQuery jwtQuery) {
    this.jwtQuery = jwtQuery;
  }

  public JWTAuthenticationRequestMatcher() {}

  public JWTAuthenticationRequestMatcher(JWTQuery jwtQuery) {
    Assert.notNull(jwtQuery);
    this.jwtQuery = jwtQuery;
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    JWTUserTagInfo tagInfo = JWTWebScopeUtil.getJWTUserTagInfo(request);
    String jwt = jwtQuery.getJWT(request);
    // 有JWT,但是没有认证成功标识.
    if (tagInfo == null && jwt != null) {
      return true;
    }
    return false;
  }
}
