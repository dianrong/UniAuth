package com.dianrong.common.uniauth.client.custom;

import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.dianrong.common.uniauth.client.custom.model.ShareDomainAuthentication;

/**
 * 用于处理多域共享信息的情况 
 * @author wanglin
 */
public class ShareDomainCasAuthenticationProvider extends CasAuthenticationProvider{

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticate = super.authenticate(authentication);
		if (shareDomainSupport(authenticate)) {
			CasAuthenticationToken orginalAuthentication = (CasAuthenticationToken)authenticate;
			return new ShareDomainAuthentication(this.getKey(),  orginalAuthentication.getPrincipal(), orginalAuthentication.getCredentials(),
					orginalAuthentication.getAuthorities(), orginalAuthentication.getUserDetails(), orginalAuthentication.getAssertion());
		} else {
			return authenticate;
		}
	}
	
	public boolean shareDomainSupport(Object obj){
		return obj instanceof CasAuthenticationToken;
	}
}
