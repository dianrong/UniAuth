package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.server.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.interfaces.read.IGroupResource;
import com.dianrong.common.uniauth.common.interfaces.rw.IGroupRWResource;

@RestController
public class GroupResource implements IGroupRWResource {

	@Autowired
	private GroupService groupService;

	@Override
	public Response<GroupDto> getGroupTree(GroupParam groupParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> addUsersIntoGroup(UserListParam userListParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> removeUsersFromGroup(UserListParam userListParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<GroupDto> addNewGroupIntoGroup(GroupParam groupParam) {
		GroupDto groupDto = groupService.createDescendantGroup(groupParam);
		return Response.success(groupDto);
	}

	@Override
	public Response<Boolean> deleteGroup(PrimaryKeyParam primaryKeyParam) {
		Boolean success = groupService.deleteGroup(primaryKeyParam.getId());
		return Response.success(success);
	}

	@Override
	public Response<GroupDto> updateGroup(GroupParam groupParam) {
		GroupDto groupDto = groupService.updateGroup(groupParam);
		return Response.success(groupDto);
	}

	@Override
	public Response<List<UserDto>> getGroupOwners(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToGroup(GroupParam groupParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> saveRolesToGroup(GroupParam groupParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
