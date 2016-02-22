package com.dianrong.common.uniauth.client.support;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.SSExpressionSecurityMetadataSource;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class SSBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	@Autowired
	private UniClientFacade uniClientFacade;

	@SuppressWarnings("unused")
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().getName().equals("org.springframework.security.web.access.intercept.FilterSecurityInterceptor")){
			FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor)bean;
			FilterInvocationSecurityMetadataSource securityMetadataSource = filterSecurityInterceptor.getSecurityMetadataSource();
			if(securityMetadataSource instanceof ExpressionBasedFilterInvocationSecurityMetadataSource){
				ExpressionBasedFilterInvocationSecurityMetadataSource expressionSecurityMetadataSource = 
							(ExpressionBasedFilterInvocationSecurityMetadataSource)securityMetadataSource;
				
				@SuppressWarnings("unchecked")
				LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = 
					(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>)ReflectionSupport.getField(expressionSecurityMetadataSource, "requestMap", true);
				
				
				
				//filterSecurityInterceptor.setSecurityMetadataSource(new SSExpressionSecurityMetadataSource(requestMap));
			}
		}
		return bean;
	}
}
