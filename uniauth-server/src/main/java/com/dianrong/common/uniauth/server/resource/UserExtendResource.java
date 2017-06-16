package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendRWResource;
import com.dianrong.common.uniauth.server.service.AttributeExtendService;
import com.dianrong.common.uniauth.server.util.BeanConverter;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 被AttributeExtendResource替代.
 *
 * @author wanglin
 * @see com.dianrong.common.uniauth.server.resource.AttributeExtendResource
 */
@Api(value = "用户扩展属性操作")
@Deprecated
@RestController
public class UserExtendResource implements IUserExtendRWResource {

  @Autowired
  private AttributeExtendService attributeExtendService;

  @ApiOperation(value = "查询用户扩展属性")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "查询条件code", dataType = "string",
          paramType = "query")})
  @Override
  @Timed
  @Deprecated
  public Response<PageDto<UserExtendDto>> searchUserExtend(UserExtendPageParam pageParam) {
    PageDto<AttributeExtendDto> pageDto = attributeExtendService.search(pageParam.getCode(),
        pageParam.getPageNumber(), pageParam.getPageSize());
    List<AttributeExtendDto> data = pageDto.getData();
    List<UserExtendDto> userExtendDto = Lists.newArrayList();
    if (data != null && !data.isEmpty()) {
      for (AttributeExtendDto aed : data) {
        userExtendDto.add(BeanConverter.convert(aed));
      }
    }
    PageDto<UserExtendDto> result = new PageDto<UserExtendDto>(pageDto.getCurrentPage(),
        pageDto.getPageSize(), pageDto.getTotalCount(), userExtendDto);
    return Response.success(result);
  }

  @ApiOperation(value = "新增用户扩展属性", notes = "不能存在两个code相同的扩展信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string",
          paramType = "query")})
  @Override
  @Deprecated
  public Response<UserExtendDto> addUserExtend(UserExtendParam userExtendParam) {
    AttributeExtendDto attributeExtendDto = attributeExtendService.add(userExtendParam.getCode(),
        null, null, userExtendParam.getDescription());
    return Response.success(BeanConverter.convert(attributeExtendDto));
  }

  @ApiOperation(value = "根据主键id更新用户扩展属性", notes = "不能存在两个code相同的扩展信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "扩展属性id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "code", dataType = "string", paramType = "query",
          defaultValue = "不传则不更新该字段"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string",
          paramType = "query")})
  @Override
  @Deprecated
  public Response<Integer> updateUserExtend(UserExtendParam userExtendParam) {
    int count = attributeExtendService.updateByKey(userExtendParam.getId(),
        userExtendParam.getCode(), null, null, userExtendParam.getDescription());
    return Response.success(count);
  }
}

