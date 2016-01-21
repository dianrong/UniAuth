package com.dianrong.common.uniauth.client;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserExt extends User {
	private static final long serialVersionUID = 8347558918889027136L;
	private String passwordSalt;
	
	public UserExt(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities, String passwordSalt) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.passwordSalt = passwordSalt;
	}

	public UserExt(String username, String password, Collection<? extends GrantedAuthority> authorities, String passwordSalt) {
		super(username, password, authorities);
		this.passwordSalt = passwordSalt;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

}
