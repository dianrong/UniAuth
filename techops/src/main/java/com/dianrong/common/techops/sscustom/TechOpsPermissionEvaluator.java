package com.dianrong.common.techops.sscustom;

import java.io.Serializable;

import org.springframework.security.core.Authentication;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluator;

public class TechOpsPermissionEvaluator implements UniauthPermissionEvaluator {

	public TechOpsPermissionEvaluator() {
		
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(authentication.getName().equals("zengwei.xu@dianrong.com")){
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return false;
	}

}
