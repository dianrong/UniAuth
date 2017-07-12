package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.request.ProfileDefinitionParam;
import com.dianrong.common.uniauth.server.service.ProfileService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.sharerw.interfaces.IProfileRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Api("Profile操作接口")
@RestController
public class ProfileResource implements IProfileRWResource {

  @Autowired
  private ProfileService profileService;

  @ApiOperation("根据ProfileId获取Profile的定义信息")
  @Override
  public Response<ProfileDefinitionDto> getProfileDefinition(Long id) {
    return Response.success(profileService.getProfileDefinition(id));
  }

  @ApiOperation("新增Profile定义")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或者tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "name", value = "名称", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "编码(唯一标识)", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "attributes", value = "扩展属性的定义map", dataType = "java.util.Map",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "descendantProfileIds", value = "子ProfileId集合",
          dataType = "java.util.Set", paramType = "query")})
  @Override
  public Response<ProfileDefinitionDto> addNewProfileDefinition(ProfileDefinitionParam param) {
    return Response.success(profileService.addNewProfileDefinition(param.getName(), param.getCode(),
        param.getDescription(), BeanConverter.convertToModel(param.getAttributes()),
        param.getDescendantProfileIds()));
  }

  @ApiOperation("更新Profile定义")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "name", value = "名称", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "编码(唯一标识)", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "attributes", value = "扩展属性的定义map", dataType = "java.util.Map",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "descendantProfileIds", value = "子ProfileId集合",
          dataType = "java.util.Set", paramType = "query")})
  @Override
  public Response<ProfileDefinitionDto> updateProfileDefinition(Long id,
      ProfileDefinitionParam param) {
    return Response.success(profileService.updateProfileDefinition(id, param.getName(),
        param.getCode(), param.getDescription(),
        BeanConverter.convertToModel(param.getAttributes()), param.getDescendantProfileIds()));
  }

  @ApiOperation("扩展Profile定义的扩展属性和子ProfileId")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "name", value = "名称", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "编码(唯一标识)", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string", required = true,
          paramType = "query"),
      @ApiImplicitParam(name = "attributes", value = "扩展属性的定义map", dataType = "java.util.Map",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "descendantProfileIds", value = "子ProfileId集合",
          dataType = "java.util.Set", paramType = "query")})
  @Override
  public Response<ProfileDefinitionDto> extendProfileDefinition(Long id,
      ProfileDefinitionParam param) {
    return Response.success(profileService.extendProfileDefinition(id,
        BeanConverter.convertToModel(param.getAttributes()), param.getDescendantProfileIds()));
  }
}
