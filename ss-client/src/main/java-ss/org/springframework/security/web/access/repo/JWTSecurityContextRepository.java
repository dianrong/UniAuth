package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.client.custom.jwt.JWTStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public class JWTSecurityContextRepository extends AbstractCacheSecurityContextRepository {

  private static final String JWT_CACHE_NAME = "JWT_CACHE";

  private final JWTQuery jwtQuery;

  public JWTSecurityContextRepository(UniauthCacheManager uniauthCacheManager, JWTQuery jwtQuery) {
    super(uniauthCacheManager);
    Assert.notNull(jwtQuery, "JWTQuery must not be null");
    this.jwtQuery = jwtQuery;
  }

  @Override
  public AuthenticationType supportedAuthenticationType() {
    return AuthenticationType.JWT;
  }

  @Override
  protected String getCacheName() {
    return JWT_CACHE_NAME;
  }

  @Override
  protected String getCacheKey(HttpServletRequest request) {
    String jwt = this.jwtQuery.getJWT(request);
    if (jwt == null) {
      return null;
    }
    return StringUtil.md5(jwt);
  }

  @Override
  public Class<? extends Authentication> supportAuthenticationClz() {
    return JWTStatelessAuthenticationSuccessToken.class;
  }
}
