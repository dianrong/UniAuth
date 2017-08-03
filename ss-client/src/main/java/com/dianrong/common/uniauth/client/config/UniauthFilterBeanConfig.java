package com.dianrong.common.uniauth.client.config;

import com.dianrong.common.uniauth.client.custom.filter.SwitchableSingleSignOutFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 配置uniauth中的filter类型的bean.
 *
 * @author wanglin
 */
@Configuration
@Conditional(UniauthFilterBeanCreateCondition.class)
public class UniauthFilterBeanConfig {

  @Autowired
  private ConfigureBeanCreator configureBeanCreator;

  @Bean(name = "singleLogoutFilter")
  public SwitchableSingleSignOutFilter getSingleLogoutFilter() {
    return configureBeanCreator.create(SwitchableSingleSignOutFilter.class);
  }

  @Bean(name = "requestSingleLogoutFilter")
  public LogoutFilter getLogoutFilter() {
    return configureBeanCreator.create(LogoutFilter.class);
  }
}
