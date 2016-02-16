package com.dianrong.common.uniauth.common.bean.request;

import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Created by Arc on 14/1/16.
 */
public class Operator {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    protected String opAccount;
    
    public Operator(){
    	try {
    		//SecurityContextHolder.getContext().getAuthentication().getPrincipal()
			Class<?> schClass = Class.forName("org.springframework.security.core.context.SecurityContextHolder");
			Method getContextMethod = schClass.getMethod("getContext", (Class<?>[])null);
			Object securityContext = getContextMethod.invoke(null, (Object[])null);
			if(securityContext != null){
				Method getAuthenticationMethod = securityContext.getClass().getMethod("getAuthentication", (Class<?>[])null);
				Object authentication = getAuthenticationMethod.invoke(null, (Object[])null);
				if(authentication != null){
					Method getPrincipalMethod = authentication.getClass().getMethod("getPrincipal", (Class<?>[])null);
					Object principal = getPrincipalMethod.invoke(null, (Object[])null);
					if(principal != null){
						opAccount = principal.toString();
					}
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
    }
}
