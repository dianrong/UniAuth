package com.dianrong.common.uniauth.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dianrong.uniauth.client.UAClientFacade;
import com.dianrong.uniauth.common.data.UAUserDetailInfo;

public class SSUserDetailService implements UserDetailsService {
	private UAClientFacade uaClientFacade;
	private DomainDefine domainDefine;
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		if(userName != null && !"".equals(userName.trim())){
			String domainName = domainDefine.getDomainName();
			UAUserDetailInfo uaudi = uaClientFacade.detailInfo(userName, domainName);
			
			if(uaudi != null){
				String email = uaudi.getEmail();
				String phone = uaudi.getPhone();
				String password = uaudi.getPassword();
				String passwordSalt = uaudi.getPasswordSalt();
				Byte status = uaudi.getStatus();
				List<String> roleList = uaudi.getRoleList();
				boolean accountStatus = status == 0? true: false;
				
				Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
				if(roleList != null && roleList.size() > 0){
					for(String role : roleList){
						role = role.startsWith("ROLE_") ? role : "ROLE_" + role;
						SimpleGrantedAuthority auth = new SimpleGrantedAuthority(role);
						auths.add(auth);
					}
				}
				
				//login by mail
				if(userName.indexOf("@") != -1){
					User user = new UserExt(email, password, true, true, true, accountStatus, auths, passwordSalt);
					return user;
				}
				else{
					User user = new UserExt(phone, password, true, true, true, accountStatus, auths, passwordSalt);
					return user;
				}
			}
		}
		throw new UsernameNotFoundException(userName + " not found");
	}
	public UAClientFacade getUaClientFacade() {
		return uaClientFacade;
	}
	public void setUaClientFacade(UAClientFacade uaClientFacade) {
		this.uaClientFacade = uaClientFacade;
	}
	public DomainDefine getDomainDefine() {
		return domainDefine;
	}
	public void setDomainDefine(DomainDefine domainDefine) {
		this.domainDefine = domainDefine;
	}
	
}