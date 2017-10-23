package org.springframework.security.web.access.repo;

import com.dianrong.common.uniauth.client.custom.basicauth.BasicAuthStatelessAuthenticationSuccessToken;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

public class UniauthCasSecurityContextRepository
    extends HttpSessionSecurityContextRepository implements UniauthSecurityContextRepository {

  @Override
  public boolean support(AuthenticationType authenticationType) {
    return AuthenticationType.CAS.isSupported(authenticationType);
  }


  @Override
  public Class<? extends Authentication> supportAuthenticationClz() {
    return CasAuthenticationToken.class;
  }
}
