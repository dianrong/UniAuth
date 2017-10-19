package com.dianrong.common.uniauth.server.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.*;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.enm.UserType;
import com.dianrong.common.uniauth.server.service.UserService;
import com.dianrong.common.uniauth.server.service.multidata.DelegateUserAuthentication;
import com.dianrong.common.uniauth.server.support.audit.ResourceAudit;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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

  @ResourceAudit
  @Override
  public Response<UserDto> addNewUser(UserParam userParam) {
    return Response.success(userService.addNewUser(userParam.getName(), userParam.getPhone(),
        userParam.getEmail(), userParam.getType()));
  }

  @ResourceAudit
  @Override
  public Response<UserDto> updateUser(UserParam userParam) {
    return Response.success(userService.updateUser(userParam.getUserActionEnum(), userParam.getId(),
        userParam.getAccount(), userParam.getTenancyId(), userParam.getTenancyCode(),
        userParam.getName(), userParam.getPhone(), userParam.getEmail(), userParam.getType(),
        userParam.getPassword(), userParam.getOriginPassword(),
        userParam.getIgnorePwdStrategyCheck(), userParam.getStatus()));
  }

  @Override
  @Timed
  public Response<List<RoleDto>> getAllRolesToUserAndDomain(UserParam userParam) {
    return Response
        .success(userService.getAllRolesToUser(userParam.getId(), userParam.getDomainId()));
  }

  @ResourceAudit
  @Override
  public Response<Void> saveRolesToUser(UserParam userParam) {
    userService.saveRolesToUser(userParam.getId(), userParam.getRoleIds());
    return Response.success();
  }

  @ApiOperation("根据条件分页查询用户信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "name", value = "用户姓名(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "phone", value = "用户电话(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "exactPhone", value = "用户电话(精确匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "email", value = "用户邮箱(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "exactEmail", value = "用户邮箱(精确匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "account", value = "用户账号(邮箱,电话)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "status", value = "状态(0,1)", dataType = "java.lang.Integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "java.lang.Integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页数", dataType = "java.lang.Integer",
          paramType = "query")})
  @Override
  @Timed
  public Response<PageDto<UserDto>> searchUser(UserQuery userQuery) {
    PageDto<UserDto> pageDto = userService.searchUser(userQuery.getUserId(), userQuery.getGroupId(),
        userQuery.getNeedDescendantGrpUser(), userQuery.getNeedDisabledGrpUser(),
        userQuery.getRoleId(), userQuery.getUserIds(), userQuery.getExcludeUserIds(),
        userQuery.getName(), userQuery.getPhone(), userQuery.getExactPhone(), userQuery.getEmail(),
        userQuery.getExactEmail(), userQuery.getAccount(), userQuery.getType(),
        userQuery.getStatus(), userQuery.getTagId(), userQuery.getNeedTag(),
        userQuery.getPageNumber(), userQuery.getPageSize());
    return Response.success(pageDto);
  }

  @ApiOperation("与searchusers接口类似,但是该接口不区分租户.")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "name", value = "用户姓名(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "phone", value = "用户电话(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "exactPhone", value = "用户电话(精确匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "email", value = "用户邮箱(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "exactEmail", value = "用户邮箱(精确匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "account", value = "用户账号(邮箱,电话)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "status", value = "状态(0,1)", dataType = "java.lang.Integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "分页大小", dataType = "java.lang.Integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页数", dataType = "java.lang.Integer",
          paramType = "query")})
  @Override
  @Timed
  public Response<PageDto<UserDto>> searchUserWithoutTenantConcern(UserQuery userQuery) {
    PageDto<UserDto> pageDto = userService.searchUser(null, null, false, false, null, null, null,
        userQuery.getName(), userQuery.getPhone(), userQuery.getExactPhone(), userQuery.getEmail(),
        userQuery.getExactEmail(), userQuery.getAccount(), UserType.NORMAL, userQuery.getStatus(),
        null, false, userQuery.getPageNumber(), userQuery.getPageSize(), true);
    return Response.success(pageDto);
  }

  @ResourceAudit
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
    return Response.success(delegateUserAuthentication.login(loginParam));
  }

  @ResourceAudit
  @ApiOperation("系统账号登陆接口")
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
  public Response<UserDto> systemLogin(LoginParam loginParam) {
    return Response.success(userService.systemLogin(loginParam));
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

  @ResourceAudit
  @Override
  public Response<Void> resetPassword(UserParam userParam) {
    userService.resetPassword(userParam);
    return Response.success();
  }

  @ResourceAudit
  @Override
  public Response<Void> replaceRolesToUser(UserParam userParam) {
    userService.replaceRolesToUser(userParam.getId(), userParam.getRoleIds(),
        userParam.getDomainId());
    return Response.success();
  }

  @Override
  public Response<List<UserDto>> searchUsersWithRoleCheck(PrimaryKeyParam primaryKeyParam) {
    return Response.success(userService.searchUsersWithRoleCheck(primaryKeyParam.getId()));
  }

  @Override
  public Response<List<UserDto>> searchUsersWithTagCheck(PrimaryKeyParam primaryKeyParam) {
    return Response.success(userService.searchUsersWithTagCheck(primaryKeyParam.getId()));
  }

  @Override
  public Response<List<TagDto>> getTagsWithUserCheckedInfo(UserParam userParam) {
    return Response
        .success(userService.searchTagsWithUserChecked(userParam.getId(), userParam.getDomainId()));
  }

  @ResourceAudit
  @Override
  public Response<Void> replaceTagsToUser(UserParam userParam) {
    userService.replaceTagsToUser(userParam.getId(), userParam.getTagIds());
    return Response.success();
  }

  @Override
  public Response<List<UserDto>> searchUserByRoleId(UserParam userParam) {
    return Response.success(userService.searchUserByRoleIds(userParam.getRoleIds()));
  }

  @Override
  public Response<List<UserDto>> searchUserByTagId(UserParam userParam) {
    return Response.success(userService.searchUserByTagIds(userParam.getTagIds()));
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

  @ResourceAudit
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

  @ResourceAudit
  @ApiOperation("更新用户关联的IPA账号.在更新之前需要验证IPA账号和密码是否正确.")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "account", value = "用户账号", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "ipa", value = "新的IPA账号", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "ipaPassword", value = "IPA密码", required = true, dataType = "string",
          paramType = "query")})
  @Override
  public Response<Void> updateUserIPAAccount(UserParam userParam) {
    userService.updateUserIPAAccount(userParam.getAccount(), userParam.getTenancyId(),
        userParam.getTenancyCode(), userParam.getIpaAccount(), userParam.getIpaPassword());
    return Response.success();
  }
}
