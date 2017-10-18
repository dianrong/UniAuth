package org.springframework.security.web.access.repo;

import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

public class UniauthHttpSessionSecurityContextRepository
    extends HttpSessionSecurityContextRepository implements UniauthSecurityContextRepository {

  @Override
  public boolean support(AuthenticationType authenticationType) {
    return AuthenticationType.CAS.isSupported(authenticationType);
  }
}
