package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.server.service.UserService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserRWResource;

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
				userParam.getPassword(),userParam.getOriginPassword(),userParam.getStatus());
		return Response.success(userDto);
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
		PageDto<UserDto> pageDto = userService.searchUser(userQuery.getUserId(), userQuery.getGroupId(), userQuery.getNeedDescendantGrpUser(),  userQuery.getNeedDisabledGrpUser(), 
				userQuery.getRoleId(), userQuery.getUserIds(), userQuery.getExcludeUserIds(), userQuery.getName(),userQuery.getPhone(),userQuery.getEmail() ,userQuery.getStatus(), userQuery.getTagId(), userQuery.getNeedTag(), userQuery.getPageNumber(),userQuery.getPageSize());
		return Response.success(pageDto);
	}

	@Override
	public Response<UserDto> login(LoginParam loginParam) {
		UserDto dto = userService.login(loginParam);
		return Response.success(dto);
	}

	@Override
	public Response<UserDetailDto> getUserDetailInfoByUid(UserParam userParam) {
		UserDetailDto userDetailDto = userService.getUserDetailInfoByUid(userParam.getId());
		return new Response<UserDetailDto>(userDetailDto);
	}

	@Override
	@Timed
	public Response<UserDetailDto> getUserDetailInfo(LoginParam loginParam) {
		UserDetailDto userDetailDto = userService.getUserDetailInfo(loginParam);
		return new Response<UserDetailDto>(userDetailDto);
	}

	@Override
	public Response<UserDto> getSingleUser(UserParam userParam) {
		return new Response<UserDto>(userService.getSingleUser(userParam));
	}
	
	@Override
	public Response<UserDto> getUserInfoByUserTag(LoginParam loginParam) {
		return new Response<UserDto>(userService.getUserByEmailOrPhone(loginParam));
	}
	
	@Override
	public Response<Void> resetPassword(UserParam userParam) {
		userService.resetPassword(userParam);
		return Response.success();
	}

	@Override
	public Response<Void> replaceRolesToUser(UserParam userParam) {
		userService.replaceRolesToUser(userParam.getId(), userParam.getRoleIds(), userParam.getDomainId());
		return Response.success();
	}

	@Override
	public Response<List<UserDto>> searchUsersWithRoleCheck(PrimaryKeyParam primaryKeyParam) {
		List<UserDto> userDtos = userService.searchUsersWithRoleCheck(primaryKeyParam.getId());
		return Response.success(userDtos);
	}

	@Override
	public Response<List<UserDto>> searchUsersWithTagCheck(PrimaryKeyParam primaryKeyParam) {
		List<UserDto> userDtos = userService.searchUsersWithTagCheck(primaryKeyParam.getId());
		return Response.success(userDtos);
	}
	
	@Override
	public Response<List<TagDto>> getTagsWithUserCheckedInfo(UserParam userParam) {
		List<TagDto> tagDtos = userService.searchTagsWithUserChecked(userParam.getId(), userParam.getDomainId());
		return Response.success(tagDtos);
	}
	
	@Override
	public Response<Void> replaceTagsToUser(UserParam userParam) {
		userService.replaceTagsToUser(userParam.getId(), userParam.getTagIds());
		return Response.success();
	}

	@Override
	public Response<List<UserDto>> searchUserByRoleId(UserParam userParam) {
		List<UserDto> userDtos = userService.searchUserByRoleIds(userParam.getRoleIds());
		return Response.success(userDtos);
	}
	
	@Override
	public Response<List<UserDto>> searchUserByTagId(UserParam userParam) {
		List<UserDto> userDtos = userService.searchUserByTagIds(userParam.getTagIds());
		return Response.success(userDtos);
	}

    @Override
    public Response<List<UserDto>> getUsersByGroupCodeRoleIds(UserParam userParam) {
        return Response.success(userService.getUsersByGroupCodeRoleIds(userParam.getGroupCode(), userParam.getIncludeSubGrp(), userParam.getIncludeRoleIds()));
    }
}
