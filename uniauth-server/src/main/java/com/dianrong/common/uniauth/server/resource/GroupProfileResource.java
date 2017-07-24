package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.ProfileParam;
import com.dianrong.common.uniauth.server.service.GroupProfileService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.dianrong.common.uniauth.sharerw.interfaces.IGroupProfileRWResource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Group的Profile相关操作.
 */
@Api("组的Profile值操作接口")
@RestController
public class GroupProfileResource implements IGroupProfileRWResource {

  @Autowired
  private GroupProfileService groupProfileService;

  @ApiOperation("根据Uniauth中的组Id和ProfileId获取用户的属性集合")
  @Timed
  @Override
  public Response<Map<String, Object>> getGroupProfile(Integer groupId, Long profileId, Long time) {
    return Response.success(groupProfileService.getGroupProfile(groupId, profileId, time));
  }

  @ApiOperation("更新一个组的扩展属性值(如果扩展属性不存在,则添加),随后返回对应ProfileId的Profile.")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户Id(或者传tenancyCode)", dataType = "long",
          required = true, paramType = "query"),
      @ApiImplicitParam(name = "attributes", value = "更新的扩展属性值集合", dataType = "java.util.Map",
          required = true, paramType = "query")})
  @Timed
  @Override
  public Response<Map<String, Object>> addOrUpdateGrpProfile(Integer groupId, Long profileId,
      ProfileParam param) {
    return Response.success(groupProfileService.addOrUpdateGrpProfile(groupId, profileId,
        BeanConverter.convertToModel(param.getAttributes())));
  }
}
