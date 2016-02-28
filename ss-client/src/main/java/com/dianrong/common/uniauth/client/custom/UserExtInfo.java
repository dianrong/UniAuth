package com.dianrong.common.uniauth.client.custom;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserDto getUserDto() {
		return userDto;
	}

	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

	public DomainDto getDomainDto() {
		return domainDto;
	}

	public void setDomainDto(DomainDto domainDto) {
		this.domainDto = domainDto;
	}
	
}
