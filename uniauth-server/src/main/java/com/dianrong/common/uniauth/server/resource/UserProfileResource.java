package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.UserIdentityType;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.server.service.UserProfileService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserProfileRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户Profile操作相关接口.
 */
@Api("用户的Profile值操作接口")
@RestController
public class UserProfileResource implements IUserProfileRWResource {

  @Autowired
  private UserProfileService userProfileService;

  @ApiOperation("根据Uniauth中的用户Id和ProfileId获取用户的属性集合")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "uniauthId", value = "用户在Uniauth系统中的唯一Id", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "profileId", value = "Profile定义的Id", dataType = "long",
          required = true, paramType = "query")})
  @Timed
  @Override
  public Response<Map<String, Object>> getUserProfile(Long uniauthId, Long profileId) {
    return Response.success(userProfileService.getUserProfile(uniauthId, profileId));
  }

  @ApiOperation("根据用户的Identity和Identity类型和ProfileId获取用户的属性集合(启用用户)")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "profileId", value = "Profile定义的Id", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "identity", value = "用户的唯一标识值", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "identityType", value = "identity的类型", dataType = "string",
          required = true, paramType = "query",
          allowableValues = "EMAIL, PHONE, STAFF_NO, LDAP_DN, USER_GUID")})
  @Timed
  @Override
  public Response<Map<String, Object>> getUserProfileByIdentity(String identity, Long profileId,
      UserIdentityType identityType) {
    return Response
        .success(userProfileService.getUserProfileByIdentity(identity, profileId, identityType));
  }

  @ApiOperation("更新一个用户的扩展属性值(如果扩展属性不存在,则添加),随后返回对应ProfileId的Profile.")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "uniauthId", value = "用户在Uniauth系统中的唯一Id", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "profileId", value = "Profile定义的Id", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "attributes", value = "更新的扩展属性值集合", dataType = "java.util.Map",
          required = true, paramType = "query")})
  @Timed
  @Override
  public Response<Map<String, Object>> addOrUpdateUserProfile(Long uniauthId, Long profileId,
      ProfileParam param) {
    return Response
    .success(userProfileService.addOrUpdateUserProfile(uniauthId, profileId, BeanConverter.convertToModel(param.getAttributes())));
  }
}
