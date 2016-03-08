package com.dianrong.common.uniauth.common.bean.request;

import com.dianrong.common.uniauth.common.util.ReflectionUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Arc on 14/1/16.
 */
public class Operator {
	@JsonProperty(access = JsonProperty.Access.READ_WRITE)
	protected Long opUserId;

	public Operator() {
		try {
			// SecurityContextHolder.getContext().getAuthentication().getPrincipal()
			Class<?> clazz = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
			if (clazz != null) {
				Object securityContext = ReflectionUtils.invokeStaticMethodWithoutParam(clazz, "getContext");
				if (securityContext != null) {
					Object authentication = ReflectionUtils.invokeMethodWithoutParam(securityContext, "getAuthentication");
					if (authentication != null) {
						Object principal = ReflectionUtils.invokeMethodWithoutParam(authentication, "getPrincipal");
						if (principal != null) {
							opUserId = (Long) ReflectionUtils.invokeMethodWithoutParam(principal, "getId");
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public Long getOpUserId() {
		return opUserId;
	}
}
