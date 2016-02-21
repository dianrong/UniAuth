package com.dianrong.common.uniauth.client.support;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;

public class UserExtInfo extends User {
	private static final long serialVersionUID = 8347558918889027136L;
	private Long id;
	private UserDto userDto;
	private DomainDto domainDto;
	
	public UserExtInfo(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, 
			Long id, UserDto userDto, DomainDto domainDto) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		this.userDto = userDto;
		this.domainDto = domainDto;
	}

	public UserExtInfo(String username, String password, Collection<? extends GrantedAuthority> authorities, 
			Long id, UserDto userDto, DomainDto domainDto) {
		super(username, password, authorities);
		this.id = id;
		this.userDto = userDto;
		this.domainDto = domainDto;
	}
}
