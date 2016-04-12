package org.springframework.security.web.access.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.custom.SSExpressionSecurityMetadataSource;
import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UrlRoleMappingDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;

public class SSBeanPostProcessor implements BeanPostProcessor {
	private static Logger LOGGER = LoggerFactory.getLogger(SSBeanPostProcessor.class);
	@Autowired
	private UniClientFacade uniClientFacade;

	@Autowired
	private DomainDefine domainDefine;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		String currentDomainCode = domainDefine.getDomainCode();
		String beanClazzName = bean.getClass().getName();
		
		if(beanClazzName.equals(FilterSecurityInterceptor.class.getName())){
			CheckDomainDefine.checkDomainDefine(currentDomainCode);
			//currentDomainCode = currentDomainCode.substring(AppConstants.ZK_DOMAIN_PREFIX.length());
			
			FilterSecurityInterceptor filterSecurityInterceptor = (FilterSecurityInterceptor)bean;
			//note: access public secure object is not allowed, this is a bit too overkilled if set to be true
			filterSecurityInterceptor.setRejectPublicInvocations(domainDefine.isRejectPublicInvocations());
			FilterInvocationSecurityMetadataSource securityMetadataSource = filterSecurityInterceptor.getSecurityMetadataSource();
			if(securityMetadataSource instanceof ExpressionBasedFilterInvocationSecurityMetadataSource){
				ExpressionBasedFilterInvocationSecurityMetadataSource expressionSecurityMetadataSource = (ExpressionBasedFilterInvocationSecurityMetadataSource)securityMetadataSource;
				
				@SuppressWarnings("unchecked")
				LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = 
					(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>)ReflectionUtils.getField(expressionSecurityMetadataSource, "requestMap", true);

				DomainParam domainParam = new DomainParam();
				domainParam.setCode(currentDomainCode);
				Response<List<UrlRoleMappingDto>> response = null;
				while(true){
					try{
						response = uniClientFacade.getPermissionResource().getUrlRoleMapping(domainParam);
						break;
					}catch(Exception e){
						LOGGER.warn("The uniauth server not completely started yet, need sleeping for 2 seconds.");
						try {
							Thread.sleep(2000L);
						} catch (InterruptedException ie) {
						}
					}
				}
				List<UrlRoleMappingDto> urlRoleMappingDtoList = response.getData();
				
				LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> appendMap = convert2StandardMap(urlRoleMappingDtoList);
				
				requestMap.putAll(appendMap);
				
				filterSecurityInterceptor.setSecurityMetadataSource(new SSExpressionSecurityMetadataSource(requestMap));
			}
		}
		return bean;
	}
	
	private LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> convert2StandardMap(List<UrlRoleMappingDto> urlRoleMappingDtoList){
		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> appendMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		if(urlRoleMappingDtoList != null){
			SpelExpressionParser spelParser = new SpelExpressionParser();
			
			Map<SSUrlAndMethod,Set<String>> plainMap = new HashMap<SSUrlAndMethod,Set<String>>();
			
			for(UrlRoleMappingDto urlRoleMappingDto: urlRoleMappingDtoList){
				String permUrl = urlRoleMappingDto.getPermUrl();
				String roleCode = urlRoleMappingDto.getRoleCode();
				String permType = urlRoleMappingDto.getPermType();
				String httpMethod = urlRoleMappingDto.getHttpMethod();
				
				SSUrlAndMethod urlAndMethod = new SSUrlAndMethod();
				urlAndMethod.setHttpMethod(httpMethod);
				urlAndMethod.setPermUrl(permUrl);
				/*
				 * 
				if(PermTypeEnum.PRIVILEGE.toString().equals(permType)){
					permUrl = permUrl.startsWith("/") ? permUrl : "/" + permUrl;
				}
				*/
				
				Set<String> roleCodeSet = plainMap.get(urlAndMethod);
				if(roleCodeSet == null){
					roleCodeSet = new HashSet<String>();
					roleCodeSet.add(roleCode);
					plainMap.put(urlAndMethod, roleCodeSet);
				}
				else{
					roleCodeSet.add(roleCode);
				}
			}
			
			Iterator<Entry<SSUrlAndMethod,Set<String>>> plainIterator = plainMap.entrySet().iterator();
			while(plainIterator.hasNext()){
				Entry<SSUrlAndMethod,Set<String>> plainEntry = plainIterator.next();
				SSUrlAndMethod urlAndMethod = plainEntry.getKey();
				String permUrl = urlAndMethod.getPermUrl();
				String httpMethod = urlAndMethod.getHttpMethod();
				Set<String> plainSet = plainEntry.getValue();
				
				httpMethod = httpMethod == null || "".equals(httpMethod.trim()) || "ALL".equals(httpMethod.trim()) ? null : httpMethod;
				
				//case insensitive for url
				RegexRequestMatcher rrm = new RegexRequestMatcher(permUrl, httpMethod);
				
				StringBuilder sb = new StringBuilder();
				
				String[] plainRoleCodes = plainSet.toArray(new String[0]);
				if(plainRoleCodes.length == 1){
					sb.append("hasRole('" + plainRoleCodes[0] + "')");
				}
				else{
					for(int i = 0;i < plainRoleCodes.length;i++){
						if(i == 0){
							sb.append("hasAnyRole('" + plainRoleCodes[i] + "',");
						}
						else if(i == plainRoleCodes.length - 1){
							sb.append("'" + plainRoleCodes[i] + "')" );
						}
						else{
							sb.append("'" + plainRoleCodes[i] + "'," );
						}
					}
				}
				
				WebExpressionConfigAttribute weca = new WebExpressionConfigAttribute(spelParser.parseExpression(sb.toString()));
				List<ConfigAttribute> wecaList = new ArrayList<ConfigAttribute>();
				wecaList.add(weca);
				
				appendMap.put(rrm, wecaList);
			}
		}
		return appendMap;
	}

}
