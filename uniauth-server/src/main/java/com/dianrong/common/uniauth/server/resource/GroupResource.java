package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.GroupParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.common.interfaces.IGroupResource;

public class GroupResource implements IGroupResource {

	@Override
	public Response<GroupDto> getGroupTree() {
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> deleteGroup(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> updateGroup(GroupParam groupParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<UserDto>> getGroupOwners(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
