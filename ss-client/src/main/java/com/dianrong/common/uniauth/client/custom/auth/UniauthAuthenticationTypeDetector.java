package com.dianrong.common.uniauth.client.custom.auth;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthDetector;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import javax.servlet.http.HttpServletRequest;

/**
 * 检测顺序为: 1 BasicAuth类型 2 JWT类型 3 默认为CAS类型.
 */
public class UniauthAuthenticationTypeDetector implements AuthenticationTypeDetector {

  private final JWTQuery jwtQuery;

  public UniauthAuthenticationTypeDetector(JWTQuery jwtQuery) {
    Assert.notNull(jwtQuery, "JWTQuery must not be null");
    this.jwtQuery = jwtQuery;
  }

  @Override
  public AuthenticationType detect(HttpServletRequest request, Object... args) {
    if (BasicAuthDetector.isBasicAuthRequest(request)) {
      return AuthenticationType.BASIC_AUTH;
    }
    if (this.jwtQuery.getJWT(request) != null) {
      return AuthenticationType.JWT;
    }
    // 默认为CAS类型的验证方式.
    return AuthenticationType.CAS;
  }
}
