package com.dianrong.common.uniauth.client;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class SSAccessDecisionManager implements AccessDecisionManager {

	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if(configAttributes != null && !configAttributes.isEmpty()){
			Iterator<ConfigAttribute> ite = configAttributes.iterator();
			while (ite.hasNext()) {
				ConfigAttribute ca = ite.next();
				String needRole = ((SecurityConfig) ca).getAttribute();
				for (GrantedAuthority ga : authentication.getAuthorities()) {
					if (needRole.equals(ga.getAuthority())) {
						return;
					}
				}
			}
		}
		String denyMsg = "No rights to access " + object.toString();
		throw new AccessDeniedException(denyMsg);
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
