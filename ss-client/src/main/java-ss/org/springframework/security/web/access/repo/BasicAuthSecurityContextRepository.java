package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuth;
import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthDetector;
import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.common.cache.UniauthCacheManager;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;

@Slf4j
public class BasicAuthSecurityContextRepository extends AbstractCacheSecurityContextRepository {

  /**
   * 缓存的名称.
   */
  public static final String BASIC_AUTH_CACHE_NAME = "BASIC_AUTH_CACHE";

  public BasicAuthSecurityContextRepository(UniauthCacheManager uniauthCacheManager) {
    super(uniauthCacheManager);
  }

  @Override
  public AuthenticationType supportedAuthenticationType() {
    return AuthenticationType.BASIC_AUTH;
  }

  @Override
  protected String getCacheName() {
    return BASIC_AUTH_CACHE_NAME;
  }

  @Override
  protected String getCacheKey(HttpServletRequest request) {
    try {
      BasicAuth basicAuth = BasicAuthDetector.getBasicAuthInfo(request);
      if (basicAuth != null) {
        return basicAuth.getMd5();
      }
    } catch (UnsupportedEncodingException ex) {
      log.error("Failed to get basic auth info from request.", ex);
    }
    return null;
  }

  @Override
  public Class<? extends Authentication> supportAuthenticationClz() {
    return BasicAuthStatelessAuthenticationSuccessToken.class;
  }
}
