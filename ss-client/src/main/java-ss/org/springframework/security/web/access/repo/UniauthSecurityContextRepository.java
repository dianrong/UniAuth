package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.SecurityContextRepository;

public interface UniauthSecurityContextRepository extends SecurityContextRepository {

  /**
   * 返回支持的AuthenticationType,不能为空.
   * @see AuthenticationType
   */
  AuthenticationType supportedAuthenticationType();


  /**
   * 返回当前Repo支持的Authentication类型.
   */
  Class<? extends Authentication> supportAuthenticationClz();
}
