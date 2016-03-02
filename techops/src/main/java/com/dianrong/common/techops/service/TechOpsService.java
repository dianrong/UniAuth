package com.dianrong.common.techops.service;

import java.util.*;

import javax.annotation.Resource;

import com.dianrong.common.techops.bean.LoginUser;
import com.dianrong.common.techops.helper.PrimitiveConverter;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.DomainDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.DomainParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.enm.PermTypeEnum;
import com.dianrong.common.uniauth.sharerw.facade.UARWFacade;

@Service
public class TechOpsService {

    @Resource
    private UARWFacade uARWFacade;
    
    public UserExtInfo getLoginUserInfo(){
    	UserExtInfo userExtInfo = (UserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	return userExtInfo;
    }
    
	public LoginUser getLoginUser(){
		LoginUser loginUser = new LoginUser();
		UserExtInfo userExtInfo = this.getLoginUserInfo();
		DomainDto domainDto = userExtInfo.getDomainDto();
		List<String> authorizedDomainCodeList = new ArrayList<String>();
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		List<RoleDto> returnRoleList = new ArrayList<RoleDto>();
		// construct returnUser
		UserDto returnUserDto = new UserDto();
		returnUserDto.setId(userExtInfo.getUserDto().getId());
		returnUserDto.setEmail(userExtInfo.getUserDto().getEmail());
		returnUserDto.setName(userExtInfo.getUserDto().getName());
		returnUserDto.setPhone(userExtInfo.getUserDto().getPhone());

		Map<String, Set<String>> returnPermMap = new TreeMap<>();

		if(domainDto != null){
			List<RoleDto> roleDtoList = domainDto.getRoleList();
			if(roleDtoList != null && !roleDtoList.isEmpty()){
				for(RoleDto roleDto: roleDtoList){
					// construct returnRole
					RoleDto returnRoleDto = PrimitiveConverter.convert(roleDto);
					returnRoleList .add(returnRoleDto);
					String roleCode = roleDto.getRoleCode();
					// construct returnPermissions
					Map<String, Set<String>> rolePermMap = roleDto.getPermMap();
					if(rolePermMap != null && rolePermMap.size() > 0) {
						Set<String> rolePermMapKeySet = rolePermMap.keySet();
						for(String key : rolePermMapKeySet) {
							Set<String> returnPermValue = returnPermMap.get(key);
							if(returnPermValue != null) {
								returnPermValue.addAll(rolePermMap.get(key));
							} else {
								Set<String> set = new HashSet<>();
								set.addAll(rolePermMap.get(key));
								returnPermMap.put(key, set);
							}
						}
					}

					if(AppConstants.ROLE_ADMIN.equals(roleCode) || AppConstants.ROLE_SUPER_ADMIN.equals(roleCode)){
						Set<String> domainCodeList = rolePermMap.get(PermTypeEnum.DOMAIN.toString());
						if(domainCodeList != null && !domainCodeList.isEmpty()){
							for(String domainCode: domainCodeList){
								if(!authorizedDomainCodeList.contains(domainCode)){
									authorizedDomainCodeList.add(domainCode);
								}
							}
						}
					}
				}
			}
			
			if(!authorizedDomainCodeList.isEmpty()){
				DomainParam domainParam = new DomainParam();
				if(!authorizedDomainCodeList.contains(AppConstants.DOMAIN_CODE_TECHOPS)){
					domainParam.setDomainCodeList(authorizedDomainCodeList);
				}
				// construct switchable domainDtoList.
				domainDtoList = uARWFacade.getDomainRWResource().getAllLoginDomains(domainParam).getData();
			}
		}
		loginUser.setPermMap(returnPermMap);
		loginUser.setRoles(returnRoleList);
		loginUser.setSwitchableDomains(domainDtoList);
		loginUser.setUser(returnUserDto);
		return loginUser;
	}

}
