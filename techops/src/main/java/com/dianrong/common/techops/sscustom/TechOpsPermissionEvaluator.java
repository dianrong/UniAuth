package com.dianrong.common.techops.sscustom;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.*;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TechOpsPermissionEvaluator extends UniauthPermissionEvaluatorImpl {

	@Autowired
	private UniClientFacade uniClientFacade;
	
	public TechOpsPermissionEvaluator() {
		
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetObject, Object permission) {
		if(AppConstants.PERM_GROUP_OWNER.equals(permission)){
			GroupParam groupParam = null;
			if(targetObject instanceof GroupParam) {
				groupParam = (GroupParam)targetObject;
			} else if(targetObject instanceof UserListParam) {
				UserListParam userListParam = (UserListParam)targetObject;
				List<Linkage<Long, Integer>> userIdGroupIdPairs = userListParam.getUserIdGroupIdPairs();
				List<Integer> targetGroupIds = new ArrayList<>();
				if(!CollectionUtils.isEmpty(userIdGroupIdPairs)) {
					for(Linkage<Long, Integer> userIdGroupIdPair : userIdGroupIdPairs) {
						targetGroupIds.add(userIdGroupIdPair.getEntry2());
					}
				}
				groupParam = new GroupParam().setTargetGroupId(userListParam.getGroupId()).setTargetGroupIds(targetGroupIds);
			}
			if(groupParam != null) {
				Response<Void> response = uniClientFacade.getGroupResource().checkOwner(groupParam);
				List<Info> infoList = response.getInfo();
				if (CollectionUtils.isEmpty(infoList)) {
					return true;
				}
			}
		} else if(AppConstants.PERM_ROLEID_CHECK.equals(permission)) {
			RoleParam roleParam = (RoleParam)targetObject;
			RoleQuery roleQuery = new RoleQuery();
			roleQuery.setPageNumber(0);
			roleQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
			roleQuery.setId(roleParam.getId());
			roleQuery.setDomainId(roleParam.getDomainId());
			Response<PageDto<RoleDto>> pageDtoResponse = uniClientFacade.getRoleResource().searchRole(roleQuery);
			List<Info> infoList = pageDtoResponse.getInfo();
			if(CollectionUtils.isEmpty(infoList)) {
				PageDto<RoleDto> roleDtoPageDto = pageDtoResponse.getData();
				if(roleDtoPageDto != null && !CollectionUtils.isEmpty(roleDtoPageDto.getData())) {
					Integer domainId = roleDtoPageDto.getData().get(0).getDomainId();
					TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Set<Integer> domainIdSet = techOpsUserExtInfo.getDomainIdSet();
					if(domainIdSet.contains(domainId)) {
						return true;
					}
				}
			}
		} else if(AppConstants.PERM_ROLEIDS_CHECK.equals(permission)) {
			List<Integer> roleIds = null;
			if(targetObject instanceof GroupParam) {
				GroupParam groupParam = (GroupParam)targetObject;
				roleIds = groupParam.getRoleIds();
			} else if(targetObject instanceof UserParam) {
				UserParam userParam = (UserParam) targetObject;
				roleIds = userParam.getRoleIds();
			} else if(targetObject instanceof PermissionParam) {
				PermissionParam permissionParam = (PermissionParam) targetObject;
				roleIds = permissionParam.getRoleIds();
			}
			if(CollectionUtils.isEmpty(roleIds)) {
				return true;
			} else {
				RoleQuery roleQuery = new RoleQuery();
				roleQuery.setPageNumber(0);
				roleQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
				roleQuery.setRoleIds(roleIds);
				Response<PageDto<RoleDto>> pageDtoResponse = uniClientFacade.getRoleResource().searchRole(roleQuery);
				List<Info> infoList = pageDtoResponse.getInfo();
				if(CollectionUtils.isEmpty(infoList)) {
					PageDto<RoleDto> roleDtoPageDto = pageDtoResponse.getData();
					if(roleDtoPageDto != null && !CollectionUtils.isEmpty(roleDtoPageDto.getData())) {
						TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
						Set<Integer> domainIdSet = techOpsUserExtInfo.getDomainIdSet();
						List<RoleDto> roleDtos = roleDtoPageDto.getData();
						for(RoleDto roleDto : roleDtos) {
							if(!domainIdSet.contains(roleDto.getDomainId())) {
								return false;
							}
						}
						return true;
					}
				}
			}
		} else if(AppConstants.PERM_PERMID_CHECK.equals(permission)) {
			PermissionParam permissionParam = (PermissionParam)targetObject;
			Integer permId = permissionParam.getId();
			PermissionQuery permissionQuery = new PermissionQuery();
			permissionQuery.setId(permId);
			permissionQuery.setPageNumber(0);
			permissionQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
			Response<PageDto<PermissionDto>> pageDtoResponse = uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
			List<Info> infoList = pageDtoResponse.getInfo();
			if(CollectionUtils.isEmpty(infoList)) {
				PageDto<PermissionDto> permissionDtoPageDto = pageDtoResponse.getData();
				if(permissionDtoPageDto != null && !CollectionUtils.isEmpty(permissionDtoPageDto.getData())) {
					Integer domainId = permissionDtoPageDto.getData().get(0).getDomainId();
					TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Set<Integer> domainIdSet = techOpsUserExtInfo.getDomainIdSet();
					if(domainIdSet.contains(domainId)) {
						return true;
					}
				}
			}
		} else if(AppConstants.PERM_PERMIDS_CHECK.equals(permission)) {
			RoleParam roleParam = (RoleParam)targetObject;
			List<Integer> permIds = roleParam.getPermIds();
			PermissionQuery permissionQuery = new PermissionQuery();
			permissionQuery.setPermIds(permIds);
			permissionQuery.setPageNumber(0);
			permissionQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
			Response<PageDto<PermissionDto>> pageDtoResponse = uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
			List<Info> infoList = pageDtoResponse.getInfo();
			if(CollectionUtils.isEmpty(infoList)) {
				PageDto<PermissionDto> permissionDtoPageDto = pageDtoResponse.getData();
				if(permissionDtoPageDto != null && !CollectionUtils.isEmpty(permissionDtoPageDto.getData())) {
					List<PermissionDto> permissionDtos = permissionDtoPageDto.getData();
					TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					Set<Integer> domainIdSet = techOpsUserExtInfo.getDomainIdSet();
					for(PermissionDto permissionDto : permissionDtos) {
						if(!domainIdSet.contains(permissionDto.getDomainId())) {
							return false;
						}
					}
					return true;
				}
			}
		}

		return super.hasPermission(authentication, targetObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return super.hasPermission(authentication, targetId, targetType, permission);
	}
	
	
}
