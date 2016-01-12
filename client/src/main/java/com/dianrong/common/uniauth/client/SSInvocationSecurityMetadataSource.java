package com.dianrong.common.uniauth.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.dianrong.common.uniauth.client.support.PatternMatchMost;
import com.dianrong.uniauth.client.UAClientFacade;
import com.dianrong.uniauth.common.data.UAUriPatternRolesMapping;

public class SSInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
	private UAClientFacade uaClientFacade;
	private DomainDefine domainDefine;
	private Map<Pattern, List<ConfigAttribute>> resourceMap = new HashMap<Pattern, List<ConfigAttribute>>();
	private List<ConfigAttribute> allRoleList = new ArrayList<ConfigAttribute>();
	private List<ConfigAttribute> emptyRoleList = new ArrayList<ConfigAttribute>();

	/**	
 	public SSInvocationSecurityMetadataSource() {
		loadResourceDefine();
	}*/

	private void loadResourceDefine() {
		String domainName = domainDefine.getDomainName();
		List<UAUriPatternRolesMapping> mappingList = uaClientFacade.getUriPatternRolesMapping(domainName);
		if(mappingList != null && !mappingList.isEmpty()){
			for(UAUriPatternRolesMapping mapping: mappingList){
				String uriPattern = mapping.getUriPattern();
				List<String> roleList = mapping.getRoleList();
				
				Pattern pattern = Pattern.compile(uriPattern);
				List<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
				
				if(roleList != null && !roleList.isEmpty()){
					for(String role: roleList){
						role = role.startsWith("ROLE_") ? role : "ROLE_" + role;
						ConfigAttribute ca = new SecurityConfig(role);
						atts.add(ca);
					}
				}
				allRoleList.addAll(atts);
				resourceMap.put(pattern, atts);
			}
		}
	}

	// According to a URL, Find out permission configuration of this URL.
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		// guess object is a URL.
		String requestUrl = ((FilterInvocation) object).getRequestUrl();
		Iterator<Pattern> patterns = resourceMap.keySet().iterator();
		Pattern matchMostPattern = PatternMatchMost.findMachMost(patterns, requestUrl);
		if(matchMostPattern != null){
			return resourceMap.get(matchMostPattern);
		}
		else{
			return emptyRoleList;
		}
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return allRoleList;
	}

	public UAClientFacade getUaClientFacade() {
		return uaClientFacade;
	}

	public void setUaClientFacade(UAClientFacade uaClientFacade) {
		this.uaClientFacade = uaClientFacade;
	}

	public DomainDefine getDomainDefine() {
		return domainDefine;
	}

	public void setDomainDefine(DomainDefine domainDefine) {
		this.domainDefine = domainDefine;
	}
	
}