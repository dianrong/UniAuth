package com.dianrong.common.techops.sscustom;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Linkage;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.*;
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
		String perm = (String)permission;
		GroupParam groupParam = null;
		RoleQuery roleQuery;
		RoleParam roleParam;
		PermissionQuery permissionQuery;
		Response<PageDto<PermissionDto>> pageDtoPermissionResponse;
		List<Info> infoList;
		switch (perm) {
			case AppConstants.PERM_GROUP_OWNER:
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
					infoList = response.getInfo();
					if (CollectionUtils.isEmpty(infoList)) {
						return true;
					}
				}
				break;
			case AppConstants.PERM_ROLEID_CHECK:
				roleParam = (RoleParam)targetObject;
				roleQuery = new RoleQuery();
				roleQuery.setPageNumber(0);
				roleQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
				roleQuery.setId(roleParam.getId());
				roleQuery.setDomainId(roleParam.getDomainId());
				Response<PageDto<RoleDto>> pageDtoResponse = uniClientFacade.getRoleResource().searchRole(roleQuery);
				infoList = pageDtoResponse.getInfo();
				if(CollectionUtils.isEmpty(infoList)) {
					PageDto<RoleDto> roleDtoPageDto = pageDtoResponse.getData();
					if(roleDtoPageDto != null && !CollectionUtils.isEmpty(roleDtoPageDto.getData())) {
						Integer domainId = roleDtoPageDto.getData().get(0).getDomainId();
						if(domainIdSet.contains(domainId)) {
							return true;
						}
					}
				}
				break;
			case AppConstants.PERM_ROLEIDS_CHECK:
				List<Integer> roleIds = null;
				if(targetObject instanceof GroupParam) {
					groupParam = (GroupParam)targetObject;
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
					roleQuery = new RoleQuery();
					roleQuery.setPageNumber(0);
					roleQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
					roleQuery.setRoleIds(roleIds);
					Response<PageDto<RoleDto>> pageRoleDtoResponse = uniClientFacade.getRoleResource().searchRole(roleQuery);
					infoList = pageRoleDtoResponse.getInfo();
					if(CollectionUtils.isEmpty(infoList)) {
						PageDto<RoleDto> roleDtoPageDto = pageRoleDtoResponse.getData();
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
				break;
			case AppConstants.PERM_PERMID_CHECK:
				PermissionParam permissionParam = (PermissionParam)targetObject;
				Integer permId = permissionParam.getId();
				permissionQuery = new PermissionQuery();
				permissionQuery.setId(permId);
				permissionQuery.setPageNumber(0);
				permissionQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
				pageDtoPermissionResponse = uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
				infoList = pageDtoPermissionResponse.getInfo();
				if(CollectionUtils.isEmpty(infoList)) {
					PageDto<PermissionDto> permissionDtoPageDto = pageDtoPermissionResponse.getData();
					if(permissionDtoPageDto != null && !CollectionUtils.isEmpty(permissionDtoPageDto.getData())) {
						Integer domainId = permissionDtoPageDto.getData().get(0).getDomainId();
						if(domainIdSet.contains(domainId)) {
							return true;
						}
					}
				}
				break;
			case AppConstants.PERM_PERMIDS_CHECK:
				roleParam = (RoleParam)targetObject;
				List<Integer> permIds = roleParam.getPermIds();
				permissionQuery = new PermissionQuery();
				permissionQuery.setPermIds(permIds);
				permissionQuery.setPageNumber(0);
				permissionQuery.setPageSize(AppConstants.MAX_PAGE_SIZE);
				pageDtoPermissionResponse = uniClientFacade.getPermissionResource().searchPerm(permissionQuery);
				infoList = pageDtoPermissionResponse.getInfo();
				if(CollectionUtils.isEmpty(infoList)) {
					PageDto<PermissionDto> permissionDtoPageDto = pageDtoPermissionResponse.getData();
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
				break;
			case AppConstants.PERM_TAGTYPEID_CHECK:
				Set<Integer> tagTypeIdSet = new HashSet<Integer>();
				if(!CollectionUtils.isEmpty(domainIdSet)) {
					TagTypeQuery tagTypeQuery = new TagTypeQuery();
					tagTypeQuery.setDomainIds(new ArrayList<Integer>(domainIdSet));
					List<TagTypeDto> tagTypeDtos = uniClientFacade.getTagResource().searchTagTypes(tagTypeQuery).getData();
					if(!CollectionUtils.isEmpty(tagTypeDtos)) {
						for(TagTypeDto tagTypeDto : tagTypeDtos) {
							tagTypeIdSet.add(tagTypeDto.getId());
						}
					}
					TagTypeParam tagTypeParam = (TagTypeParam)targetObject;
					if(tagTypeIdSet.contains(tagTypeParam.getId())) {
						return true;
					}
				} else {
					return false;
				}
				break;
			case AppConstants.PERM_TAGID_CHECK:
				TagParam tagParam = (TagParam)targetObject;
				TagQuery tagQuery = new TagQuery();
				tagQuery.setId(tagParam.getId()).setPageSize(AppConstants.MAX_PAGE_SIZE).setPageNumber(AppConstants.MIN_PAGE_NUMBER);
				List<TagDto> tagDtos = uniClientFacade.getTagResource().searchTags(tagQuery).getData().getData();
				if(!CollectionUtils.isEmpty(tagDtos)) {
					Integer tagTypeId = tagDtos.get(0).getTagTypeId();
					TagTypeQuery tagTypeQuery = new TagTypeQuery();
					tagTypeQuery.setId(tagTypeId);
					Integer domainId = uniClientFacade.getTagResource().searchTagTypes(tagTypeQuery).getData().get(0).getDomainId();
					if(domainIdSet.contains(domainId)) {
						return true;
					}
				} else {
					return false;
				}
				break;
		}

		return super.hasPermission(authentication, targetObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return super.hasPermission(authentication, targetId, targetType, permission);
	}
	
	
}
