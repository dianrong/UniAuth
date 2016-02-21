package com.dianrong.common.uniauth.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dianrong.common.uniauth.client.support.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.util.CheckZkConfig;

public class SSUserDetailService implements UserDetailsService {
	
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Value("#{uniauthConfig[domainDefine.domainCode]}")
	private String currentDomainCode;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		CheckZkConfig.checkZkConfig(currentDomainCode, "domain definition", "/com/dianrong/cfg/1.0.0/uniauth/domains/?");
		
		if(userName == null || "".equals(userName.toString())){
			throw new UsernameNotFoundException(userName + " not found");
		}
		else{
			LoginParam loginParam = new LoginParam();
			loginParam.setAccount(userName);
			Response<UserDetailDto> response = uniClientFacade.getUserResource().getUserDetailInfo(loginParam);
			UserDetailDto userDetailDto = response.getData();
			
			UserDto userDto = userDetailDto.getUserDto();
			Long id = userDto.getId();
			List<DomainDto> domainDtoList = userDetailDto.getDomainList();
			DomainDto currentDomainDto = null;
			
			for(DomainDto domainDto : domainDtoList){
				String domainCode = domainDto.getCode();
				if(currentDomainCode.equals(domainCode)){
					currentDomainDto = domainDto;
					break;
				}
			}
			
			Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			if(currentDomainDto != null){
				List<RoleDto> roleDtoList = currentDomainDto.getRoleList();
				for(RoleDto roleDto: roleDtoList){
					String roleCode = roleDto.getRoleCode();
					SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
					authorities.add(authority);
				}
			}
			
			return new UserExtInfo(userName, "fake_password", true, true, true, true, authorities, id, userDto, currentDomainDto);
		}
	}
}