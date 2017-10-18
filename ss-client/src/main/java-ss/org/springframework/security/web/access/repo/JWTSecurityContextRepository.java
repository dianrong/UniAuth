package org.springframework.security.web.access.repo;

import javax.servlet.http.HttpServletRequest;

import com.dianrong.common.uniauth.client.custom.jwt.JWTQuery;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import com.dianrong.common.uniauth.common.util.StringUtil;

public class JWTSecurityContextRepository extends AbstractCacheSecurityContextRepository {

  private static final String JWT_CACHE_NAME = "JWT_CACHE";

  /**
   * 获取JWT工具类, 默认值.
   */
  private final JWTQuery jwtQuery;

  public JWTSecurityContextRepository(UniauthCacheManager uniauthCacheManager, JWTQuery jwtQuery) {
    super(uniauthCacheManager);
    Assert.notNull(jwtQuery, "JWTQuery must not be null");
    this.jwtQuery = jwtQuery;
  }

  @Override
  public boolean support(AuthenticationType authenticationType) {
    return AuthenticationType.JWT.isSupported(authenticationType);
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
}
