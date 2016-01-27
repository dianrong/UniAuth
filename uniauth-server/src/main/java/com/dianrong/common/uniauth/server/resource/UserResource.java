package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import com.dianrong.common.uniauth.common.interfaces.rw.IUserRWResource;
import com.dianrong.common.uniauth.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.interfaces.read.IUserResource;

/**
 * Created by Arc on 14/1/16.
 */
@RestController
public class UserResource implements IUserRWResource {

	@Autowired
	private UserService userService;

	@Override
	public Response<UserDto> addNewUser(UserParam userParam) {
		UserDto userDto = userService.addNewUser(userParam.getName(),userParam.getPhone(),userParam.getEmail());
		return Response.success(userDto);
	}

	@Override
	public Response<UserDto> updateUser(UserParam userParam) {
		UserDto userDto = userService.updateUser(userParam.getUserActionEnum(),userParam.getId(),
				userParam.getName(),userParam.getPhone(),userParam.getEmail(),
				userParam.getPassword(),userParam.getStatus());
		return Response.success(userDto);
	}

	@Override
	public Response<String> deleteUser(PrimaryKeyParam primaryKeyParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToUser(UserParam userParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<String> saveRolesToUser(UserParam userParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response<PageDto<UserDto>> searchUser(UserQuery userQuery) {
		// TODO Auto-generated method stub
		return null;
	}
}
