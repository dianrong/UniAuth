package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.request.UserDetailInfoParam;
import com.dianrong.common.uniauth.server.service.UserDetailService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserDetailRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Api("用户详细信息接口实现")
@RestController
public class UserDetailResource implements IUserDetailRWResource {

  @Autowired
  private UserDetailService userDetailService;

  @ApiOperation("根据关联的用户id获取用户的detail信息")
  @ApiImplicitParams(value = {@ApiImplicitParam(name = "userId", value = "关联的用户id", required = true,
      dataType = "long", paramType = "query")})
  @Override
  public Response<UserDetailInfoDto> searchByUserId(UserDetailInfoParam param) {
    return Response.success(userDetailService.searchByUserId(param.getUserId()));
  }

  @ApiOperation("新增或修改用户的detail信息(所有属性更新))")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "关联的用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或者传租户code(tenancyCode))", required = true,
          dataType = "long", paramType = "query")})
  @Override
  public Response<UserDetailInfoDto> addOrUpdateUserDetail(UserDetailInfoParam param) {
    return Response.success(userDetailService.addOrUpdateUserDetail(param));
  }

  @ApiOperation("更新用户detail信息(只更新不为空的字段)")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "关联的用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或者传租户code(tenancyCode))", required = true,
          dataType = "long", paramType = "query")})
  @Override
  public Response<UserDetailInfoDto> updateUserDetail(UserDetailInfoParam param) {
    return Response.success(userDetailService.updateUserDetail(param));
  }
}
