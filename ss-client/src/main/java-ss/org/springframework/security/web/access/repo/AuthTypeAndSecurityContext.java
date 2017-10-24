package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.util.Assert;
import org.springframework.security.core.context.SecurityContext;

/**
 * 一个辅助类用于传参使用.绑定验证类型和验证结果.
 */
public class AuthTypeAndSecurityContext {

  private final SecurityContext securityContext;

  private final AuthenticationType authenticationType;

  public AuthTypeAndSecurityContext(SecurityContext securityContext,
      AuthenticationType authenticationType) {
    Assert.notNull(authenticationType);
    this.securityContext = securityContext;
    this.authenticationType = authenticationType;
  }

  public SecurityContext getSecurityContext() {
    return securityContext;
  }

  public AuthenticationType getAuthenticationType() {
    return authenticationType;
  }
}
