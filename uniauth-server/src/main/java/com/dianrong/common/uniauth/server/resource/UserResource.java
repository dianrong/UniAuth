package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleDto;
import com.dianrong.common.uniauth.common.bean.dto.TagDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.dto.VPNLoginResult;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.server.service.UserService;
import com.dianrong.common.uniauth.server.service.multidata.DelegateUserAuthentication;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 14/1/16.
 */
@Api("用户信息操作接口")
@RestController
public class UserResource implements IUserRWResource {

  @Autowired
  private UserService userService;

  @Autowired
  private DelegateUserAuthentication delegateUserAuthentication;

  @Override
  public Response<UserDto> addNewUser(UserParam userParam) {
    UserDto userDto =
        userService.addNewUser(userParam.getName(), userParam.getPhone(), userParam.getEmail());

    return Response.success(userDto);
  }

  @Override
  public Response<UserDto> updateUser(UserParam userParam) {
    UserDto userDto = userService.updateUser(userParam.getUserActionEnum(), userParam.getId(),
        userParam.getAccount(), userParam.getTenancyId(), userParam.getName(), userParam.getPhone(),
        userParam.getEmail(), userParam.getPassword(), userParam.getOriginPassword(),
        userParam.getIgnorePwdStrategyCheck(), userParam.getStatus());
    return Response.success(userDto);
  }

  @Override
  @Timed
  public Response<List<RoleDto>> getAllRolesToUserAndDomain(UserParam userParam) {
    List<RoleDto> roleDtos =
        userService.getAllRolesToUser(userParam.getId(), userParam.getDomainId());
    return Response.success(roleDtos);
  }

  @Override
  public Response<Void> saveRolesToUser(UserParam userParam) {
    userService.saveRolesToUser(userParam.getId(), userParam.getRoleIds());
    return Response.success();
  }

  /**
   * 根据条件过滤用户列表.
   */
  @Override
  @Timed
  public Response<PageDto<UserDto>> searchUser(UserQuery userQuery) {
    PageDto<UserDto> pageDto = userService.searchUser(userQuery.getUserId(), userQuery.getGroupId(),
        userQuery.getNeedDescendantGrpUser(), userQuery.getNeedDisabledGrpUser(),
        userQuery.getRoleId(), userQuery.getUserIds(), userQuery.getExcludeUserIds(),
        userQuery.getName(), userQuery.getPhone(), userQuery.getEmail(), userQuery.getAccount(),
        userQuery.getStatus(), userQuery.getTagId(), userQuery.getNeedTag(),
        userQuery.getPageNumber(), userQuery.getPageSize());
    return Response.success(pageDto);
  }

  @ApiOperation("登陆接口")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id或code", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "ip", value = "登陆用户所在ip", required = true, dataType = "string",
          paramType = "query")})
  @Override
  @Timed
  public Response<UserDto> login(LoginParam loginParam) {
    UserDto dto = delegateUserAuthentication.login(loginParam);
    return Response.success(dto);
  }

  @Override
  @Timed
  public Response<UserDetailDto> getUserDetailInfoByUid(UserParam userParam) {
    UserDetailDto userDetailDto = userService.getUserDetailInfoByUid(userParam.getId());
    return new Response<UserDetailDto>(userDetailDto);
  }

  @Override
  @Timed
  public Response<UserDetailDto> getUserDetailInfo(LoginParam loginParam) {
    UserDetailDto userDetailDto = delegateUserAuthentication.getUserDetailInfo(loginParam);
    return new Response<UserDetailDto>(userDetailDto);
  }

  @Override
  @Timed
  public Response<UserDto> getSingleUser(UserParam userParam) {
    return new Response<UserDto>(userService.getSingleUser(userParam));
  }

  @Override
  @Timed
  public Response<UserDto> getUserInfoByUserTag(LoginParam loginParam) {
    return new Response<UserDto>(delegateUserAuthentication.getUserByEmailOrPhone(loginParam));
  }

  @Override
  public Response<Void> resetPassword(UserParam userParam) {
    userService.resetPassword(userParam);
    return Response.success();
  }

  @Override
  public Response<Void> replaceRolesToUser(UserParam userParam) {
    userService.replaceRolesToUser(userParam.getId(), userParam.getRoleIds(),
        userParam.getDomainId());
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
    List<TagDto> tagDtos =
        userService.searchTagsWithUserChecked(userParam.getId(), userParam.getDomainId());
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

  @ApiOperation("根据组code和角色id列表查询用户列表")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "groupCode", value = "组code", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "includeSubGrp", value = "是否包含子组关联的用户", dataType = "boolean",
          paramType = "query", defaultValue = "false"),
      @ApiImplicitParam(name = "includeRoleIds", value = "角色id列表(用于进一步限定用户的范围). 不传,则不根据角色进行限定",
          dataType = "java.util.List", paramType = "query")})
  @Override
  public Response<List<UserDto>> getUsersByGroupCodeRoleIds(UserParam userParam) {
    return Response.success(userService.getUsersByGroupCodeRoleIds(userParam.getGroupCode(),
        userParam.getIncludeSubGrp(), userParam.getIncludeRoleIds()));
  }

  @ApiOperation("vpn登陆接口")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyCode", value = "租户code", dataType = "string",
          paramType = "query", defaultValue = "DIANRONG"),
      @ApiImplicitParam(name = "account", value = "账号", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "ip", value = "登陆用户所在ip", dataType = "string", paramType = "query")})
  @Override
  @Timed
  public Response<VPNLoginResult> vpnLogin(LoginParam loginParam) {
    return Response.success(userService.vpnLogin(loginParam));
  }
}
