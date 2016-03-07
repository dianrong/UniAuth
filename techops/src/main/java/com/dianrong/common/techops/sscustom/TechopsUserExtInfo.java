package com.dianrong.common.techops.sscustom;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;

public class TechopsUserExtInfo extends UserExtInfo {
	
	private static final long serialVersionUID = -4910632638160710758L;

	public TechopsUserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
			Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, id,
				userDto, domainDto, permMap);
	}

	public TechopsUserExtInfo(String username, String password, Collection<? extends GrantedAuthority> authorities,
			Long id, UserDto userDto, DomainDto domainDto, Map<String, Set<String>> permMap) {
		super(username, password, authorities, id, userDto, domainDto, permMap);
	}

}
