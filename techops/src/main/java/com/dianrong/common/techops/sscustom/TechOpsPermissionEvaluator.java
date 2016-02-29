package com.dianrong.common.techops.sscustom;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluator;

@Component("uniauthPermissionEvaluator")
public class TechOpsPermissionEvaluator implements UniauthPermissionEvaluator {

	public TechOpsPermissionEvaluator() {
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
