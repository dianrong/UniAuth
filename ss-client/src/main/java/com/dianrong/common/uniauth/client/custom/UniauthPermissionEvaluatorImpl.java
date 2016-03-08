package com.dianrong.common.uniauth.client.custom;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("uniauthPermissionEvaluator")
public class UniauthPermissionEvaluatorImpl implements UniauthPermissionEvaluator {
	
	private final Log logger = LogFactory.getLog(getClass());

	public UniauthPermissionEvaluatorImpl() {
		
	}

	/**
	 * @return false always
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object targetObject, Object permission) {
		logger.warn("Denying user " + authentication.getName() + " permission '" + permission + "' on object " + targetObject);
		return false;
	}

	/**
	 * @return false always
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		logger.warn("Denying user " + authentication.getName() + " permission '" + permission + "' on object with Id '" + targetId);
		return false;
	}

}
