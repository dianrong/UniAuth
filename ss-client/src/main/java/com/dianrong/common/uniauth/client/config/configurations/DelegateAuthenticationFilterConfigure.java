package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.DelegateAuthenticationFilter;
import com.dianrong.common.uniauth.client.custom.filter.UniauthAuthenticationFilter;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;
import com.dianrong.common.uniauth.common.exp.UniauthInvalidParamterException;
import com.dianrong.common.uniauth.common.util.JsonUtil;

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
    if (!checkParameters(args)) {
      throw new UniauthInvalidParamterException(
          "Create a DelegateAuthenticationFilterConfigure, but the create parameter is invalid! The args is :"
              + JsonUtil.object2Jason(args));
    }

    DelegateAuthenticationFilter authenticationFilter = new DelegateAuthenticationFilter();
    for (int i = 0; i < args.length; i++) {
      authenticationFilter.addAuthenticationFilter((UniauthAuthenticationFilter) args[i]);
    }
    authenticationFilter.setAuthenticationType(authenticationType);
    authenticationFilter.init();
    return authenticationFilter;
  }

  /**
   * Check传入的参数.
   */
  private boolean checkParameters(Object... args) {
    if (args.length < 1) {
      return false;
    }
    for (int i = 1; i < args.length; i++) {
      if (!(args[i] instanceof UniauthAuthenticationFilter)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return DelegateAuthenticationFilter.class.equals(cls);
  }
}
