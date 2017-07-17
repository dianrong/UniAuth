package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.request.UserWorkRelationshipParam;
import com.dianrong.common.uniauth.server.service.UserWorkRelationshipService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserWorkRelationshipRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 汇报线接口实现.
 */

@Api("汇报线关系处理")
@RestController
public class UserWorkRelationshipResource implements IUserWorkRelationshipRWResource {

  @Autowired
  private UserWorkRelationshipService userWorkRelationshipService;

  @ApiOperation("根据关联的用户id获取用户的汇报关系信息")
  @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "关联的用户id", required = true,
      dataType = "long", paramType = "query")})
  @Override
  public Response<UserWorkRelationshipDto> searchByUserId(UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.searchByUserId(param.getUserId()));
  }

  @ApiOperation("新增或修改用户的汇报关系(所有字段更新))")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "关联的用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或者传租户code(tenancyCode))", required = true,
          dataType = "long", paramType = "query")})
  @Override
  public Response<UserWorkRelationshipDto> addOrUpdateUserWrokRelationship(
      UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.addOrUpdateUserWrokRelationship(param));
  }

  @ApiOperation("更新用户的汇报关系(只更新不为空的字段)")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "关联的用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或者传租户code(tenancyCode))", required = true,
          dataType = "long", paramType = "query")})
  @Override
  public Response<UserWorkRelationshipDto> updateUserWrokRelationship(
      UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.updateUserWrokRelationship(param));
  }
}
