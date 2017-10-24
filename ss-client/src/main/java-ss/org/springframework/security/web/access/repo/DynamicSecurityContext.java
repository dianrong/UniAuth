package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 * 动态的SecurityContext.根据传入的不同的AuthenticationType来决定返回SecurityContext.
 *
 * @see AuthenticationType
 */

@Slf4j
public class DynamicSecurityContext implements SecurityContext {

  private final Map<AuthenticationType, SecurityContext> authTypeAndSecurityContextMap;

  private SecurityContext targetSecurityContext;

  public DynamicSecurityContext(
      AuthTypeAndSecurityContext targetAuthTypeAndSecurityContext,
      AuthTypeAndSecurityContext... AuthTypeAndSecurityContexts) {
    Assert.notNull(targetAuthTypeAndSecurityContext,
        "targetAuthTypeAndSecurityContext is required");
    targetSecurityContext = targetAuthTypeAndSecurityContext.getSecurityContext();
    authTypeAndSecurityContextMap = new HashMap<>(
        AuthTypeAndSecurityContexts.length + 1);
    authTypeAndSecurityContextMap
        .put(targetAuthTypeAndSecurityContext.getAuthenticationType(),
            targetAuthTypeAndSecurityContext.getSecurityContext());
    for (AuthTypeAndSecurityContext authTypeAndContext : AuthTypeAndSecurityContexts) {
      if (!authTypeAndSecurityContextMap
          .containsKey(authTypeAndContext.getAuthenticationType())) {
        authTypeAndSecurityContextMap
            .put(authTypeAndContext.getAuthenticationType(),
                authTypeAndContext.getSecurityContext());
      }
    }
  }

  @Override
  public Authentication getAuthentication() {
    return targetSecurityContext.getAuthentication();
  }

  @Override
  public void setAuthentication(Authentication authentication) {
    targetSecurityContext.setAuthentication(authentication);
  }

  public Map<AuthenticationType, SecurityContext> getAuthTypeAndSecurityContextMap() {
    return Collections.unmodifiableMap(authTypeAndSecurityContextMap);
  }

  public SecurityContext getDestSecurityContext(AuthenticationType authenticationType) {
    Assert.notNull(authenticationType, "authenticationType can not be null");
    SecurityContext securityContext = authTypeAndSecurityContextMap
        .get(authenticationType);
    if (securityContext instanceof DynamicSecurityContext) {
      return ((DynamicSecurityContext)securityContext).getDestSecurityContext(authenticationType);
    }
    return securityContext;
  }

  public synchronized boolean setSecurityContext(AuthenticationType authenticationType) {
    Assert.notNull(authenticationType, "authenticationType can not be null");
    SecurityContext securityContext = authTypeAndSecurityContextMap
        .get(authenticationType);
    if (securityContext != null) {
      this.targetSecurityContext = securityContext;
      return true;
    }
    log.warn("AuthenticationType {} has no SecurityContext!", authenticationType);
    return false;
  }
}
