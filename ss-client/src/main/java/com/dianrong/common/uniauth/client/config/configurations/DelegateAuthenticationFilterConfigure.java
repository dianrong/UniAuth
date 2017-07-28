package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.DelegateAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthCasAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthJWTAuthenticationFilter;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.exp.UniauthInvalidParamterException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public class DelegateAuthenticationFilterConfigure
    implements Configure<DelegateAuthenticationFilter> {

  @Value("#{domainDefine.authenticationType}")
  private AuthenticationType authenticationType;

  @Override
  public DelegateAuthenticationFilter create(Object... args) {
    if (args.length != 2 || !(args[0] instanceof UniauthCasAuthenticationFilter)
        || !(args[1] instanceof UniauthJWTAuthenticationFilter)) {
      throw new UniauthInvalidParamterException(
          "Create a DelegateAuthenticationFilterConfigure, but the create parameter is invalid!, the args is :"
              + args);
    }
    DelegateAuthenticationFilter authenticationFilter = new DelegateAuthenticationFilter();
    authenticationFilter.addAuthenticationFilter((UniauthCasAuthenticationFilter) args[0]);
    authenticationFilter.addAuthenticationFilter((UniauthJWTAuthenticationFilter) args[1]);
    authenticationFilter.setAuthenticationType(authenticationType);
    return authenticationFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return DelegateAuthenticationFilter.class.equals(cls);
  }
}
