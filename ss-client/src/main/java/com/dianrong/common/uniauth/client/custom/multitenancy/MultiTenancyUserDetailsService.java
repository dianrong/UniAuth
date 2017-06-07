package com.dianrong.common.uniauth.client.custom.multitenancy;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MultiTenancyUserDetailsService {

  UserDetails loadUserByUsername(String username, long tenancyId) throws UsernameNotFoundException;
}
