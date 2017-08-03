package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import com.dianrong.common.uniauth.client.custom.filter.SwitchableSingleSignOutFilter;
import com.dianrong.common.uniauth.common.client.enums.AuthenticationType;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * . create new SingleSignOutFilter
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public final class SingleSignOutFilterConfigure implements Configure<SwitchableSingleSignOutFilter> {

  @Value("#{domainDefine.authenticationType}")
  private AuthenticationType authenticationType;
  
  @Override
  public SwitchableSingleSignOutFilter create(Object... args) {
    SingleSignOutFilter originalSingleLogoutFilter = new SingleSignOutFilter();
    originalSingleLogoutFilter.setIgnoreInitConfiguration(true);
    SwitchableSingleSignOutFilter singleSignOutFilter = new SwitchableSingleSignOutFilter(originalSingleLogoutFilter);
    singleSignOutFilter.setAuthenticationType(authenticationType);
    return singleSignOutFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return SwitchableSingleSignOutFilter.class.equals(cls);
  }
}
