package com.dianrong.common.uniauth.client.config.configurations;

import com.dianrong.common.uniauth.client.config.Configure;
import com.dianrong.common.uniauth.client.config.UniauthConfigEnvLoadCondition;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * . create new SingleSignOutFilter
 *
 * @author wanglin
 */
@Component
@Conditional(UniauthConfigEnvLoadCondition.class)
public final class SingleSignOutFilterConfigure implements Configure<SingleSignOutFilter> {

  @Override
  public SingleSignOutFilter create() {
    SingleSignOutFilter singleLogoutFilter = new SingleSignOutFilter();
    singleLogoutFilter.setIgnoreInitConfiguration(true);
    return singleLogoutFilter;
  }

  @Override
  public boolean isSupport(Class<?> cls) {
    return SingleSignOutFilter.class.equals(cls);
  }
}
