package com.dianrong.common.uniauth.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IUserExtendRWResource;
import com.dianrong.common.uniauth.server.service.UserExtendService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@Api(value = "用户扩展属性操作")
@RestController
public class UserExtendResource implements IUserExtendRWResource {

    @Autowired
    private UserExtendService userExtendService;

    @ApiOperation(value = "查询用户扩展属性")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "integer", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "查询条件code", dataType = "string", paramType = "query"),})
    @Override
    @Timed
    public Response<PageDto<UserExtendDto>> searchUserExtend(UserExtendPageParam pageParam) {
        PageDto<UserExtendDto> pageDto = userExtendService.search(pageParam.getCode(), pageParam.getPageNumber(), pageParam.getPageSize());
        return Response.success(pageDto);
    }

    @ApiOperation(value = "新增用户扩展属性", notes = "不能存在两个code相同的扩展信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "string", paramType = "query"),})
    @Override
    public Response<UserExtendDto> addUserExtend(UserExtendParam userExtendParam) {
        UserExtendDto userExtendDto = userExtendService.add(userExtendParam.getCode(), userExtendParam.getDescription());
        return Response.success(userExtendDto);
    }

    @ApiOperation(value = "根据主键id更新用户扩展属性", notes = "不能存在两个code相同的扩展信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "扩展属性id", required = true, dataType = "long", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "code", dataType = "string", paramType = "query", defaultValue = "不传则不更新该字段"),
            @ApiImplicitParam(name = "description", value = "描述", dataType = "string", paramType = "query"),})
    @Override
    public Response<Integer> updateUserExtend(UserExtendParam userExtendParam) {
        int count = userExtendService.updateByKey(userExtendParam.getId(), userExtendParam.getCode(), userExtendParam.getDescription());
        return Response.success(count);
    }
}

