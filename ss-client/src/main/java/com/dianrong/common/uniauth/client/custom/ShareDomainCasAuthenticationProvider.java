package com.dianrong.common.uniauth.client.custom;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;

import com.dianrong.common.uniauth.client.custom.model.AllDomainUserExtInfo;
import com.dianrong.common.uniauth.client.custom.model.ShareDomainAuthentication;
import com.dianrong.common.uniauth.client.custom.model.SingleDomainUserExtInfo;
import com.dianrong.common.uniauth.common.util.ReflectionUtils;

/**
 * 用于处理多域共享信息的情况 
 * @author wanglin
 */
public class ShareDomainCasAuthenticationProvider extends CasAuthenticationProvider{

	private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Authentication authenticate = super.authenticate(authentication);
		if (needShareDomainSupport(authenticate)) {
			CasAuthenticationToken orginalAuthentication = (CasAuthenticationToken)authenticate;
			grantAuthoritesMapperProcess(orginalAuthentication.getUserDetails());
			return new ShareDomainAuthentication(this.getKey(),  orginalAuthentication.getPrincipal(), orginalAuthentication.getCredentials(),
					orginalAuthentication.getAuthorities(), orginalAuthentication.getUserDetails(), orginalAuthentication.getAssertion());
		} else {
			return authenticate;
		}
	}
	
	public boolean needShareDomainSupport(Object obj){
		return obj instanceof CasAuthenticationToken;
	}
	
	public void grantAuthoritesMapperProcess(UserDetails userDetails){
		if (!(userDetails instanceof  UserExtInfo)) {
				return;
		}
		SingleDomainUserExtInfo loginDomainUserExtInfo = (SingleDomainUserExtInfo)ReflectionUtils.getField(userDetails, "loginDomainUserExtInfo", true);
		ReflectionUtils.setUserInfoField(loginDomainUserExtInfo, "authorities", authoritiesMapper.mapAuthorities(loginDomainUserExtInfo.getAuthorities()));
		AllDomainUserExtInfo allDomainUserExtInfo = (AllDomainUserExtInfo)ReflectionUtils.getField(userDetails, "allDomainUserExtInfo", true);
		@SuppressWarnings("unchecked")
		ConcurrentHashMap<String, SingleDomainUserExtInfo> userExtInfoMap = (ConcurrentHashMap<String, SingleDomainUserExtInfo>)ReflectionUtils.getField(allDomainUserExtInfo, "userExtInfoMap", false);
		Set<String> keySet = userExtInfoMap.keySet();
		for (String key : keySet) {
			SingleDomainUserExtInfo userExtInfo = userExtInfoMap.get(key);
			ReflectionUtils.setUserInfoField(loginDomainUserExtInfo, "authorities", authoritiesMapper.mapAuthorities(userExtInfo.getAuthorities()));
		}
	}
	public void setAuthoritiesMapper(GrantedAuthoritiesMapper authoritiesMapper) {
		super.setAuthoritiesMapper(authoritiesMapper);
		this.authoritiesMapper = authoritiesMapper;
	}
}
