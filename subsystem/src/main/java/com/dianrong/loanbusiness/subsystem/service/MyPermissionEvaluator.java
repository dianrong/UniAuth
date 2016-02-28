package com.dianrong.loanbusiness.subsystem.service;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluator;

@Component(value="myPermissionEvaluator")
public class MyPermissionEvaluator implements UniauthPermissionEvaluator {

	public MyPermissionEvaluator() {
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(authentication.getName().equals("zengwei.xu@dianrong.com")){
			return true;
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
