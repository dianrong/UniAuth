package com.dianrong.common.techops.sscustom;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.PermissionDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagTypeDto;
import com.dianrong.common.uniauth.common.bean.request.*;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

public class TechOpsPermissionEvaluator extends UniauthPermissionEvaluatorImpl {

	@Autowired
	private UniClientFacade uniClientFacade;
	
	public TechOpsPermissionEvaluator() {
		
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetObject, Object permission) {
		TechOpsUserExtInfo techOpsUserExtInfo = (TechOpsUserExtInfo)authentication.getPrincipal();
		Set<Integer> domainIdSet = techOpsUserExtInfo.getDomainIdSet();
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
					for(PermissionDto permissionDto : permissionDtos) {
						if(!domainIdSet.contains(permissionDto.getDomainId())) {
							return false;
						}
					}
					return true;
				}
			}
		} else if(AppConstants.PERM_TAGTYPEID_CHECK.equals(permission)) {
			Set<Integer> tagTypeIdSet = new HashSet<Integer>();
			if(!CollectionUtils.isEmpty(domainIdSet)) {
				TagTypeQuery tagTypeQuery = new TagTypeQuery();
				tagTypeQuery.setDomainIds(new ArrayList<Integer>(domainIdSet));
				List<TagTypeDto> tagTypeDtos = uniClientFacade.getTagResource().getTagTypes(tagTypeQuery).getData();
				if(!CollectionUtils.isEmpty(tagTypeDtos)) {
					for(TagTypeDto tagTypeDto : tagTypeDtos) {
						tagTypeIdSet.add(tagTypeDto.getId());
					}
				}
				TagTypeParam tagTypeParam = (TagTypeParam)targetObject;
				if(tagTypeIdSet.contains(tagTypeParam.getId())) {
					return true;
				}
			}
			return false;
		}

		return super.hasPermission(authentication, targetObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return super.hasPermission(authentication, targetId, targetType, permission);
	}
	
	
}
