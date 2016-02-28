package com.dianrong.common.uniauth.client.custom;

import java.io.Serializable;

import org.springframework.security.core.Authentication;

public class UniauthPermissionEvaluatorImpl implements UniauthPermissionEvaluator {

	public UniauthPermissionEvaluatorImpl() {
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
