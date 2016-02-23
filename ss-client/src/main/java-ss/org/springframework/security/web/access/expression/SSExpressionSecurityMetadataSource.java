package org.springframework.security.web.access.expression;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.dianrong.common.uniauth.client.support.PatternMatchMost;

public class SSExpressionSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private Map<RequestMatcher, Collection<ConfigAttribute>> requestMap;

	public SSExpressionSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
		this.requestMap = requestMap;
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			allAttributes.addAll(entry.getValue());
		}

		return allAttributes;
	}

	public Collection<ConfigAttribute> getAttributes(Object object) {
		Map<RequestMatcher, Collection<ConfigAttribute>> allMatchedMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		
		final HttpServletRequest request = ((FilterInvocation) object).getRequest();
		for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMap.entrySet()) {
			if (entry.getKey().matches(request)) {
				allMatchedMap.put(entry.getKey(), entry.getValue());
			}
		}
		RequestMatcher requestMatcher = PatternMatchMost.findMachMostRequestMatcher(request, allMatchedMap);
		
		return requestMatcher == null ? null : allMatchedMap.get(requestMatcher);
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}
	
}
