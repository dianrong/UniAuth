package com.dianrong.common.techops.sscustom;

import java.io.Serializable;
import java.util.List;

import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import com.dianrong.common.uniauth.client.custom.UniauthPermissionEvaluatorImpl;
import com.dianrong.common.uniauth.common.bean.Info;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.client.UniClientFacade;

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
				return false;
			}

			Response<Void> response = uniClientFacade.getGroupResource().checkOwner(groupParam);
			List<Info> infoList = response.getInfo();
			if(infoList != null && !infoList.isEmpty()){
				return super.hasPermission(authentication, targetObject, permission);
			}
			else{
				return true;
			}
		}
		return super.hasPermission(authentication, targetObject, permission);
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return super.hasPermission(authentication, targetId, targetType, permission);
	}
	
	
}
