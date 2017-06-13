package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendValRWResource;
import com.dianrong.common.uniauth.server.data.entity.User;
import com.dianrong.common.uniauth.server.service.UserExtendValService;
import com.dianrong.common.uniauth.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author wenlongchen
 * @since May 16, 2016.
 */
@Api("用户扩展属性值操作")
@RestController
public class UserExtendValResource implements IUserExtendValRWResource {

  @Autowired
  private UserExtendValService userExtendValService;

  @Autowired
  private UserService userService;

  @ApiOperation(value = "根据用户id查询所有的扩展属于值")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query")})
  @Override
  public Response<List<UserExtendValDto>> searchByUserId(UserExtendValParam userExtendValParam) {
    List<UserExtendValDto> userExtendValDtos =
        userExtendValService.searchByUserId(userExtendValParam.getUserId());
    return Response.success(userExtendValDtos);
  }

  @ApiOperation(value = "新增用户扩展属性值")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "extendId", value = "扩展属性id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "value", value = "扩展属性值", dataType = "string",
          paramType = "query"),})
  @Override
  public Response<UserExtendValDto> add(UserExtendValParam userExtendValParam) {
    UserExtendValDto userExtendValDto = userExtendValService.add(userExtendValParam.getUserId(),
        userExtendValParam.getExtendId(), userExtendValParam.getValue());
    return Response.success(userExtendValDto);
  }

  @ApiOperation(value = "根据主键id删除扩展属性值")
  @ApiImplicitParams(value = {@ApiImplicitParam(name = "id", value = "扩展属性值记录id", required = true,
      dataType = "long", paramType = "query"),})
  @Override
  public Response<Integer> delById(UserExtendValParam userExtendValParam) {
    int count = userExtendValService.delById(userExtendValParam.getId());
    return Response.success(count);
  }

  @ApiOperation(value = "根据主键id更新扩展属性值")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "扩展属性值记录id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "userId", value = "用户id", dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "extendId", value = "扩展属性id", dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "value", value = "扩展值", dataType = "string", paramType = "query")})
  @Override
  public Response<Integer> updateById(UserExtendValParam userExtendValParam) {
    int count =
        userExtendValService.updateById(userExtendValParam.getId(), userExtendValParam.getUserId(),
            userExtendValParam.getExtendId(), userExtendValParam.getValue());
    return Response.success(count);
  }

  @ApiOperation(value = "根据用户id和扩展属性code分页查询扩展值")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "extendCode", value = "扩展属性code", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "queryOnlyUsed", value = "是否只查询使用中的属性", dataType = "boolean",
          paramType = "query"),})
  @Override
  @Timed
  public Response<PageDto<UserExtendValDto>> searchByUserIdAndCode(
      UserExtendValParam userExtendValParam) {
    PageDto<UserExtendValDto> pageDto =
        userExtendValService.searchByUserIdAndCode(userExtendValParam.getUserId(),
            userExtendValParam.getExtendCode(), userExtendValParam.getPageNumber(),
            userExtendValParam.getPageSize(), userExtendValParam.isQueryOnlyUsed());
    return Response.success(pageDto);
  }

  @ApiOperation(value = "根据账号和租户信息获取用户的扩展值")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "identity", value = "用户账号(邮箱或电话)", required = true,
          dataType = "string", paramType = "query"),})
  @Override
  public Response<List<UserExtendValDto>> searchByUserIdentity(
      UserExtendValParam userExtendValParam) {
    User user = userService.getUserByAccount(userExtendValParam.getIdentity(),
        userExtendValParam.getTenancyCode(), userExtendValParam.getTenancyId(), true,
        AppConstants.STATUS_ENABLED);
    List<UserExtendValDto> userExtendValDtos = userExtendValService.searchByUserId(user.getId());
    return Response.success(userExtendValDtos);
  }

  @ApiOperation(value = "根据传入的条件获取扩展属性值列表")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "extendId", value = "扩展属性id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "value", value = "扩展属性值(去匹配)", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "status", value = "扩展属性值的状态(0,1)", dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "includeDisableUserRelatedExtendVal", value = "是否包含禁用用户关联的扩展属性值",
          dataType = "boolean", defaultValue = "false", paramType = "query")})
  @Override
  @Timed
  public Response<List<UserExtendValDto>> search(UserExtendValParam userExtendValParam) {
    List<UserExtendValDto> userExtendList =
        userExtendValService.search(userExtendValParam.getExtendId(), userExtendValParam.getValue(),
            userExtendValParam.getIncludeDisableUserRelatedExtendVal());
    return Response.success(userExtendList);
  }
}

