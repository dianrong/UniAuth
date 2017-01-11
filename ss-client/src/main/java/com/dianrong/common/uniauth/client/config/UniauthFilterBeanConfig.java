package com.dianrong.common.uniauth.client.config;

import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 配置uniauth中的filter类型的bean
 * @author wanglin
 */
@Configuration
public class UniauthFilterBeanConfig {
	
	@Autowired
	private ConfigureBeanCreator configureBeanCreator;
	
	@Bean(name="singleLogoutFilter")
	@Conditional(UniauthFilterBeanCreateCondtion.class)
	public SingleSignOutFilter getSingleLogoutFilter() {
		return configureBeanCreator.create(SingleSignOutFilter.class);
	}
	
	@Bean(name="requestSingleLogoutFilter")
	@Conditional(UniauthFilterBeanCreateCondtion.class)
	public LogoutFilter getLogoutFilter() {
		return configureBeanCreator.create(LogoutFilter.class);
	}
}
