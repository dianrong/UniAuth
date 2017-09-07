package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupDto;
import com.dianrong.common.uniauth.common.bean.dto.OrganizationDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.OrganizationParam;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserListParam;
import com.dianrong.common.uniauth.server.service.GroupService;
import com.dianrong.common.uniauth.server.support.tree.TreeType;
import com.dianrong.common.uniauth.server.support.tree.TreeTypeTag;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.sharerw.interfaces.IOrganizationRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@TreeTypeTag(TreeType.ORGANIZATION) @Api("组织关系操作相关接口") @RestController @Slf4j
public class OrganizationResource implements IOrganizationRWResource {

  @Autowired private GroupService groupService;

  @ApiOperation(value = "添加用户与组织的关联关系") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "groupId", value = "组织id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "userIds", value = "用户列表", required = true, dataType = "java.util.List", paramType = "query"),
      @ApiImplicitParam(name = "normalMember", value = "普通关联关系(或owner关系)", dataType = "boolean", paramType = "query", defaultValue = "true")})
  @Override public Response<Void> addUsersIntoOrganization(UserListParam userListParam) {
    groupService.addUsersIntoGroup(userListParam.getGroupId(), userListParam.getUserIds(),
        userListParam.getNormalMember());
    return Response.success();
  }

  @ApiOperation(value = "删除用户与组织的关联关系") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userIdGrpIdPairs", value = "组织和用户的映射列表", required = true, dataType = "java.util.List", paramType = "query"),
      @ApiImplicitParam(name = "normalMember", value = "普通关联关系(或owner关系)", dataType = "boolean", paramType = "query", defaultValue = "true")})
  @Override public Response<Void> removeUsersFromOrganization(UserListParam userListParam) {
    groupService.removeUsersFromGroup(userListParam.getUserIdGroupIdPairs(),
        userListParam.getNormalMember());
    return Response.success();
  }

  @ApiOperation(value = "移动用户到指定组织中") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "groupId", value = "目标组织id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "userIdGroupIdPairs", value = "用户和组的原始关系", required = true, paramType = "query"),
      @ApiImplicitParam(name = "normalMember", value = "普通关联关系(或owner关系)", dataType = "boolean", paramType = "query", defaultValue = "true")})
  @Override public Response<Void> moveOrganizationUser(UserListParam userListParam) {
    groupService.moveUser(userListParam.getGroupId(), userListParam.getUserIdGroupIdPairs(),
        userListParam.getNormalMember());
    return Response.success();
  }

  @ApiOperation(value = "向父组织中添加子组织", notes = "每一个组织的code都需要是唯一的") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "targetGroupId", value = "父组织id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "子组织code", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "name", value = "子组织名称", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "description", value = "子组织描述", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "status", value = "子组织状态(0,1)", dataType = "integer", paramType = "query", allowableValues = "0,1")})
  @Override public Response<OrganizationDto> addNewOrganizationIntoOrganization(
      OrganizationParam organizationParam) {
    GroupDto groupDto = groupService.createDescendantGroup(organizationParam);
    return Response.success(BeanConverter.convert(groupDto));
  }

  @ApiOperation(value = "根据主键id更新组织信息", notes = "每一个组织的code都是唯一的") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "组织code(不能为空)", required = true, dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "name", value = "组织名称", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "description", value = "组织描述", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "status", value = "组状态(0,1)", dataType = "integer", paramType = "query", allowableValues = "0,1")})
  @Override
  public Response<OrganizationDto> updateOrganization(OrganizationParam organizationParam) {
    GroupDto groupDto = groupService
        .updateGroup(organizationParam.getId(), organizationParam.getCode(),
            organizationParam.getName(), organizationParam.getStatus(),
            organizationParam.getDescription());
    return Response.success(BeanConverter.convert(groupDto));
  }

  @ApiOperation(value = "根据主键id删除组织(与更新组织的区别是,该接口会明确将组组织下的所有关系删除)") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long", paramType = "query")})
  @Override
  public Response<OrganizationDto> deleteOrganization(OrganizationParam organizationParam) {
    GroupDto grpDto = groupService.deleteGroup(organizationParam.getId());
    return Response.success(BeanConverter.convert(grpDto));
  }

  @ApiOperation(value = "移动组织") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "目标组织id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "targetGroupId", value = "目标组织新的父组织id", required = true, dataType = "long", paramType = "query"),})
  @Override public Response<Void> moveOrganization(OrganizationParam organizationParam) {
    groupService.moveGroup(organizationParam.getId(), organizationParam.getTargetGroupId());
    return Response.success();
  }

  @ApiOperation(value = "根据条件以树的形式返回组织关系信息") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "id", value = "根组织id(如果和code都不传，则查整棵树)", dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "根组织code(如果和id都不传，则查整棵树)", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "onlyShowGroup", value = "是否只显示组织信息(不然将会返回最关联的用户)", dataType = "boolean", paramType = "query"),
      @ApiImplicitParam(name = "userGroupType", value = "用户与组织的关联关系(0,1),在onlyShowGroup=false的时候必传", dataType = "integer", paramType = "query", allowableValues = "0,1"),
      @ApiImplicitParam(name = "needOwnerMarkup", value = "是否将userId与树中每个组织的owner关系返回", dataType = "boolean", paramType = "query"),
      @ApiImplicitParam(name = "opUserId", value = "查询组织与该userId的owner关系", dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "includeDisableUser", value = "当onlyShowGroup=false时,用于指定返回的用户列表是否包含禁用用户", dataType = "boolean", paramType = "query", defaultValue = "false"),})
  @Timed @Override public Response<OrganizationDto> getOrganizationTree(OrganizationParam param) {
    GroupDto grpDto = groupService
        .getGroupTree(param.getId(), param.getCode(), param.getOnlyShowGroup(),
            param.getUserGroupType(), null, null,
            param.getNeedOwnerMarkup(), param.getOpUserId(), param.getIncludeDisableUser());
    return Response.success(BeanConverter.convert(grpDto));
  }


  @ApiOperation(value = "根据组织id查询所有与其有owner关系的用户列表") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "组织id", required = true, dataType = "long", paramType = "query")})
  @Override public Response<List<UserDto>> getOrganizationOwners(PrimaryKeyParam primaryKeyParam) {
    return Response.success(groupService.getGroupOwners(primaryKeyParam.getId()));
  }

  @ApiOperation(value = "判断操作用户与某(些)组织是否存在owner关系") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "opUserId", value = "用户id", required = true, dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "targetOrganizationIds", value = "组织id列表", dataType = "java.util.List", paramType = "query"),
      @ApiImplicitParam(name = "targetOrganizationId", value = "组织id(与targetOrganizationIds至少存在一个)", dataType = "long", paramType = "query"),})
  @Override public Response<Void> checkOwner(OrganizationParam organizationParam) {
    groupService.checkOwner(organizationParam.getOpUserId(), organizationParam.getTargetGroupIds(),
        organizationParam.getTargetGroupId());
    return Response.success();
  }
}
