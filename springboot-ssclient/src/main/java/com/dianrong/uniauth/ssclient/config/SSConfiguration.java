package com.dianrong.uniauth.ssclient.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ImportResource({"classpath:spring-context.xml"})
public class SSConfiguration {
	
	// 配置view resolver
	@Bean
	public InternalResourceViewResolver internalResourceViewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}
	
	@Bean
	public FilterRegistrationBean springSecurityFilterChain() {
		FilterRegistrationBean filter = new FilterRegistrationBean(new DelegatingFilterProxy());
		filter.addUrlPatterns("/*");
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean characterEncodingFilter(){
		CharacterEncodingFilter cfilter =  new CharacterEncodingFilter();
		cfilter.setForceEncoding(true);
		cfilter.setEncoding("UTF-8");
		FilterRegistrationBean filter = new FilterRegistrationBean(cfilter);
		filter.addUrlPatterns("/*");
		return filter;
	}
}
