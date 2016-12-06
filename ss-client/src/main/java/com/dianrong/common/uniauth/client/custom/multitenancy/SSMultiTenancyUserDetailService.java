package com.dianrong.common.uniauth.client.custom.multitenancy;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.client.custom.UserInfoCallBack;
import com.dianrong.common.uniauth.client.custom.model.UserExtInfoParam;
import com.dianrong.common.uniauth.client.support.CheckDomainDefine;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.client.DomainDefine;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

public class SSMultiTenancyUserDetailService implements MultiTenancyUserDetailsService {
	private static Logger LOGGER = Logger.getLogger(SSMultiTenancyUserDetailService.class);
	@Autowired
	private UniClientFacade uniClientFacade;
	
	@Autowired
	private DomainDefine domainDefine;
	
	@Autowired(required = false)
	private UserInfoCallBack userInfoCallBack;
	
	@Override
	public UserDetails loadUserByUsername(String userName, long tenancyId) throws UsernameNotFoundException, DataAccessException {
		String currentDomainCode = domainDefine.getDomainCode();
		String userInfoClass = domainDefine.getUserInfoClass();
		
		CheckDomainDefine.checkDomainDefine(currentDomainCode);
		
		if(userName == null || "".equals(userName.toString())){
			throw new UsernameNotFoundException(userName + " not found");
		}
		else{
			LoginParam loginParam = new LoginParam();
			loginParam.setAccount(userName);
			loginParam.setTenancyId((int)tenancyId);
			Response<UserDetailDto> response = uniClientFacade.getUserResource().getUserDetailInfo(loginParam);
			UserDetailDto userDetailDto = response.getData();
			
			if(userDetailDto == null){
				throw new UsernameNotFoundException(userName + " not found");
			}
			else{
				UserDto userDto = userDetailDto.getUserDto();
				Long id = userDto.getId();
				List<DomainDto> domainDtoList = userDetailDto.getDomainList();
				// DomainDto currentDomainDto = null;
				Map<String, UserExtInfoParam> userExtInfos = new HashMap<>();
				if(domainDtoList != null && !domainDtoList.isEmpty()){
					List<DomainDto> _domainDtoList = null; 
					if (!domainDefine.isUseAllDomainUserInfoShareMode()) {
						_domainDtoList = new ArrayList<DomainDto>();
						for(DomainDto domainDto : domainDtoList){
							String domainCode = domainDto.getCode();
							if(currentDomainCode.equals(domainCode)){
								_domainDtoList.add(domainDto);
								break;
							}
						}
					} else {
						_domainDtoList = domainDtoList;
					}
					for(DomainDto domainDto : _domainDtoList){
						Map<String, Set<String>> permMap = new HashMap<String, Set<String>>();
						Map<String, Set<PermissionDto>> permDtoMap = new HashMap<>();
						Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
						List<RoleDto> roleDtoList = domainDto.getRoleList();
						if(roleDtoList != null && !roleDtoList.isEmpty()){
							for(RoleDto roleDto: roleDtoList){
								String roleCode = roleDto.getRoleCode();
								SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleCode);
								authorities.add(authority);
								mergePermMap(permMap, roleDto.getPermMap());
								mergePermMap(permDtoMap, roleDto.getPermDtoMap());
							}
						}
						UserExtInfoParam userExtInfoParam = new UserExtInfoParam();
						userExtInfoParam.setUsername(userName).setPassword("fake_password").setEnabled(true).setAccountNonExpired(true)
						.setCredentialsNonExpired(true).setAccountNonLocked(true).setAuthorities(authorities).setId(id).setUserDto(userDto)
						.setDomainDto(domainDto).setPermMap(permMap).setPermDtoMap(permDtoMap);
						userExtInfos.put(domainDto.getCode(), userExtInfoParam);
					}
				}
				UserExtInfoParam currentDomainUserInfo = userExtInfos.get(currentDomainCode);
				if (currentDomainUserInfo == null) {
						UserExtInfoParam userExtInfoParam = new UserExtInfoParam();
						userExtInfoParam.setUsername(userName).setPassword("fake_password").setEnabled(true).setAccountNonExpired(true)
						.setCredentialsNonExpired(true).setAccountNonLocked(true).setAuthorities(new ArrayList<GrantedAuthority>()).setId(id).setUserDto(userDto)
						.setDomainDto(new DomainDto()).setPermMap(new HashMap<String, Set<String>>()).setPermDtoMap(new HashMap<String, Set<PermissionDto>>());
						userExtInfos.put(currentDomainCode, userExtInfoParam);
						currentDomainUserInfo = userExtInfos.get(currentDomainCode);
				} 
				if(userInfoClass == null || "".equals(userInfoClass.trim())){
					return UserExtInfo.build(currentDomainUserInfo , userExtInfos);
				}
				else{
					try{
						Class<?> clazz = Class.forName(userInfoClass);
						Constructor<?> construct = clazz.getConstructor(String.class, String.class, Boolean.TYPE, Boolean.TYPE,Boolean.TYPE,Boolean.TYPE, Collection.class, Long.class, UserDto.class, DomainDto.class, Map.class, Map.class);
						UserExtInfo userExtInfo = (UserExtInfo)construct.newInstance(currentDomainUserInfo.getUsername(), 
								currentDomainUserInfo.getPassword(), currentDomainUserInfo.isEnabled(), currentDomainUserInfo.isAccountNonExpired(), currentDomainUserInfo.isCredentialsNonExpired(), 
								currentDomainUserInfo.isAccountNonLocked(), currentDomainUserInfo.getAuthorities(), currentDomainUserInfo.getId(), currentDomainUserInfo.getUserDto(), 
								currentDomainUserInfo.getDomainDto(), currentDomainUserInfo.getPermMap(), currentDomainUserInfo.getPermDtoMap());
						if(userInfoCallBack != null){
							userInfoCallBack.fill(userExtInfo);
						}
						return userExtInfo;
					}catch(Exception e){
						LOGGER.error("Prepare to use ss-client's UserExtInfo, not the subsystem's customized one, possible reasons:\n (1) " + userInfoClass + " not found. \n (2) " + userInfoClass + " is not a instance of UserExtInfo.\n (3) userInfoCallBack.fill(userExtInfo) error.", e);
						return UserExtInfo.build(currentDomainUserInfo, userExtInfos);
					}
				}
			}
		}
	}
	
	private <T> void mergePermMap(Map<String, Set<T>> permMap, Map<String, Set<T>> subPermMap){
		Set<Entry<String, Set<T>>> subEntrySet = subPermMap.entrySet();
		Iterator<Entry<String, Set<T>>> subEntryIterator = subEntrySet.iterator();
		while(subEntryIterator.hasNext()){
			Entry<String, Set<T>> subEntry = subEntryIterator.next();
			String permTypeName = subEntry.getKey();
			Set<T> permValueSet = subEntry.getValue();
			
			if(permMap.containsKey(permTypeName)){
				permMap.get(permTypeName).addAll(permValueSet);
			}
			else{
				permMap.put(permTypeName, permValueSet);
			}
		}
	}

}