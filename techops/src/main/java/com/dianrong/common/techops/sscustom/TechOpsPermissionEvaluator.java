package com.dianrong.common.techops.sscustom;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.dianrong.common.uniauth.client.custom.UserExtInfo;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.request.RoleParam;
import com.dianrong.common.uniauth.common.bean.request.RoleQuery;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

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
				groupParam = new GroupParam().setTargetGroupId(userListParam.getGroupId());
			} else {
				return super.hasPermission(authentication, targetObject, permission);
			}

			Response<Void> response = uniClientFacade.getGroupResource().checkOwner(groupParam);
			List<Info> infoList = response.getInfo();
			if(infoList != null && !infoList.isEmpty()){
				return super.hasPermission(authentication, targetObject, permission);
			}
			else{
				return true;
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
		}

		return super.hasPermission(authentication, targetObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return super.hasPermission(authentication, targetId, targetType, permission);
	}
	
	
}
