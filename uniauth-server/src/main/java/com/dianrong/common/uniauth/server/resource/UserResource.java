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
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;

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
	public Response<Void> updateUser(UserParam userParam) {
		userService.updateUser(userParam.getUserActionEnum(),userParam.getId(),
				userParam.getName(),userParam.getPhone(),userParam.getEmail(),
				userParam.getPassword(),userParam.getStatus());
		return Response.success();
	}

	@Override
	public Response<List<RoleDto>> getAllRolesToUserAndDomain(UserParam userParam) {
		List<RoleDto> roleDtos = userService.getAllRolesToUser(userParam.getId(),userParam.getDomainId());
		return Response.success(roleDtos);
	}

	@Override
	public Response<Void> saveRolesToUser(UserParam userParam) {
		userService.saveRolesToUser(userParam.getId(),userParam.getRoleIds());
		return Response.success();
	}

	@Override
	public Response<PageDto<UserDto>> searchUser(UserQuery userQuery) {
		PageDto<UserDto> pageDto = userService.searchUser(userQuery.getName(),userQuery.getPhone(),userQuery.getEmail(),userQuery.getPageNumber(),userQuery.getPageSize());
		return Response.success(pageDto);
	}
}
