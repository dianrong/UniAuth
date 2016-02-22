package org.springframework.security.web.access.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.client.support.ReflectionSupport;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class SSBeanPostProcessor implements BeanPostProcessor {
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Value("#{domainDefine.domainCode}")
	private String currentDomainCode;

	@SuppressWarnings("unused")
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean.getClass().getName().equals("org.springframework.security.web.access.intercept.FilterSecurityInterceptor")){
			CheckDomainDefine.checkDomainDefine(currentDomainCode);
			
			FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor)bean;
			FilterInvocationSecurityMetadataSource securityMetadataSource = filterSecurityInterceptor.getSecurityMetadataSource();
			if(securityMetadataSource instanceof ExpressionBasedFilterInvocationSecurityMetadataSource){
				ExpressionBasedFilterInvocationSecurityMetadataSource expressionSecurityMetadataSource = 
							(ExpressionBasedFilterInvocationSecurityMetadataSource)securityMetadataSource;
				
				@SuppressWarnings("unchecked")
				LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = 
					(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>)ReflectionSupport.getField(expressionSecurityMetadataSource, "requestMap", true);
				
				DomainParam domainParam = new DomainParam();
				domainParam.setCode(currentDomainCode);
				Response<List<UrlRoleMappingDto>> response = uniClientFacade.getPermissionResource().getUrlRoleMapping(domainParam);
				List<UrlRoleMappingDto> urlRoleMappingDtoList = response.getData();
				
				Iterator<Entry<RequestMatcher, Collection<ConfigAttribute>>> iterator = requestMap.entrySet().iterator();
				while(iterator.hasNext()){
					Entry<RequestMatcher, Collection<ConfigAttribute>> entry = iterator.next();
					Collection<ConfigAttribute> oriValueCollection =  entry.getValue();
					
					String strValue = oriValueCollection.iterator().next().toString();
					
					SpelExpressionParser parser = new SpelExpressionParser();
					WebExpressionConfigAttribute weca = new WebExpressionConfigAttribute(parser.parseRaw(strValue));
					
					Collection<ConfigAttribute> processValueCollection = new ArrayList<ConfigAttribute>();
					processValueCollection.add(weca);
					
					entry.setValue(processValueCollection);
				}
				//filterSecurityInterceptor.setSecurityMetadataSource(new SSExpressionSecurityMetadataSource(requestMap));
			}
		}
		return bean;
	}
}
