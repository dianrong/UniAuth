package com.dianrong.common.techops.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
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
    
	public List<DomainDto> getDropDownDomainList(String ip){
		List<DomainDto> domainDtoList = new ArrayList<DomainDto>();
		UserExtInfo userExtInfo = getLoginUserInfo();
		DomainDto domainDto = userExtInfo.getDomainDto();
		List<String> authorizedDomainCodeList = new ArrayList<String>();
		if(domainDto != null){
			List<RoleDto> roleDtoList = domainDto.getRoleList();
			if(roleDtoList != null && !roleDtoList.isEmpty()){
				for(RoleDto roleDto: roleDtoList){
					String roleCode = roleDto.getRoleCode();
					if(AppConstants.ROLE_ADMIN.equals(roleCode) || AppConstants.ROLE_SUPER_ADMIN.equals(roleCode)){
						Map<String, List<String>> roleMap = roleDto.getPermMap();
						List<String> domainCodeList = roleMap.get(PermTypeEnum.DOMAIN.toString());
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
				domainDtoList = uARWFacade.getDomainRWResource().getAllLoginDomains(domainParam).getData();
			}
		}

		return domainDtoList;
	}

	public List<RoleDto> getAllRoles() {
		return null;
	}

	public List<PermissionDto> getAllPermissions() {
		return null;
	}
}
