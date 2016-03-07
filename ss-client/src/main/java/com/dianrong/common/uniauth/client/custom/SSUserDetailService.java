package com.dianrong.common.uniauth.client.custom;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dianrong.common.uniauth.client.DomainDefine;
import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class SSUserDetailService implements UserDetailsService {
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Autowired
	private DomainDefine domainDefine;
	
	@Autowired(required = false)
	private UserInfoCallBack userInfoCallBack;
	
	private static Logger LOGGER = LoggerFactory.getLogger(SSUserDetailService.class);
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		String currentDomainCode = domainDefine.getDomainCode();
		String userInfoClass = domainDefine.getUserInfoClass();
		
		CheckDomainDefine.checkDomainDefine(currentDomainCode);
		//currentDomainCode = currentDomainCode.substring(AppConstants.ZK_DOMAIN_PREFIX.length());
		
		if(userName == null || "".equals(userName.toString())){
			throw new UsernameNotFoundException(userName + " not found");
		}
		else{
			LoginParam loginParam = new LoginParam();
			loginParam.setAccount(userName);
			Response<UserDetailDto> response = uniClientFacade.getUserResource().getUserDetailInfo(loginParam);
			UserDetailDto userDetailDto = response.getData();
			
			if(userDetailDto == null){
				throw new UsernameNotFoundException(userName + " not found");
			}
			else{
				UserDto userDto = userDetailDto.getUserDto();
				Long id = userDto.getId();
				List<DomainDto> domainDtoList = userDetailDto.getDomainList();
				DomainDto currentDomainDto = null;
				
				if(domainDtoList != null && !domainDtoList.isEmpty()){
					for(DomainDto domainDto : domainDtoList){
						String domainCode = domainDto.getCode();
						if(currentDomainCode.equals(domainCode)){
							currentDomainDto = domainDto;
							break;
						}
					}
				}
				Map<String, Set<String>> permMap = new HashMap<String, Set<String>>();
				
				Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
				if(currentDomainDto != null){
					List<RoleDto> roleDtoList = currentDomainDto.getRoleList();
					if(roleDtoList != null && !roleDtoList.isEmpty()){
						for(RoleDto roleDto: roleDtoList){
							String roleCode = roleDto.getRoleCode();
							SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
							authorities.add(authority);
							
							mergePermMap(permMap, roleDto.getPermMap());
						}
					}
				}
				
				if(userInfoClass == null || "".equals(userInfoClass.trim())){
					return new UserExtInfo(userName, "fake_password", true, true, true, true, authorities, id, userDto, currentDomainDto, permMap);
				}
				else{
					try{
						Class<?> clazz = Class.forName(userInfoClass);
						Constructor<?> construct = clazz.getConstructor(String.class, String.class, Boolean.TYPE, Boolean.TYPE,Boolean.TYPE,Boolean.TYPE, Collection.class, Long.class, UserDto.class, DomainDto.class, Map.class);
						UserExtInfo userExtInfo = (UserExtInfo)construct.newInstance(userName, "fake_password", true, true, true, true, authorities, id, userDto, currentDomainDto, permMap);
						
						if(userInfoCallBack != null){
							userInfoCallBack.fill(userExtInfo);
						}
						return userExtInfo;
					}catch(Exception e){
						LOGGER.error("Use built-in UserExtInfo, not the customized one, error reasons guess:\n (1) " + userInfoClass + " not found. \n (2) " + userInfoClass + " is not a instance of UserExtInfo.\n (3) userInfoCallBack.fill(userExtInfo) error.", e);
						return new UserExtInfo(userName, "fake_password", true, true, true, true, authorities, id, userDto, currentDomainDto, permMap);
					}
				}
			}
		}
	}
	
	private void mergePermMap(Map<String, Set<String>> permMap, Map<String, Set<String>> subPermMap){
		Set<Entry<String, Set<String>>> subEntrySet = subPermMap.entrySet();
		Iterator<Entry<String, Set<String>>> subEntryIterator = subEntrySet.iterator();
		while(subEntryIterator.hasNext()){
			Entry<String, Set<String>> subEntry = subEntryIterator.next();
			String permTypeName = subEntry.getKey();
			Set<String> permValueSet = subEntry.getValue();
			
			if(permMap.containsKey(permTypeName)){
				permMap.get(permTypeName).addAll(permValueSet);
			}
			else{
				permMap.put(permTypeName, permValueSet);
			}
		}
	}
}