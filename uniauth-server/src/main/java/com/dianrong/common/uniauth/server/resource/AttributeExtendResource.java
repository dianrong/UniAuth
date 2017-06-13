package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.AttributeExtendDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendPageParam;
import com.dianrong.common.uniauth.common.bean.request.AttributeExtendParam;
import com.dianrong.common.uniauth.common.interfaces.readwrite.IAttributeExtendRWResource;
import com.dianrong.common.uniauth.server.service.AttributeExtendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "扩展属性操作")
@RestController
public class AttributeExtendResource implements IAttributeExtendRWResource {

  @Autowired
  private AttributeExtendService attributeExtendService;

  @ApiOperation(value = "用户扩展属性")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "integer",
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "查询条件code", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "category", value = "类型", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "subcategory", value = "子类型", dataType = "string",
          paramType = "query")})
  @Override
  @Timed
  public Response<PageDto<AttributeExtendDto>> searchAttributeExtend(
      AttributeExtendPageParam pageParam) {
    PageDto<AttributeExtendDto> pageDto = attributeExtendService.search(pageParam.getCode(),
        pageParam.getPageNumber(), pageParam.getPageSize());
    return Response.success(pageDto);
  }

  @ApiOperation(value = "新增扩展属性", notes = "不能存在两个code相同的扩展信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "tenancyId", value = "租户id(或租户code)", required = true,
          dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "code", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "category", value = "类型", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "subcategory", value = "子类型", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string",
          paramType = "query")})
  @Override
  public Response<AttributeExtendDto> addAttributeExtend(
      AttributeExtendParam attributeExtendParam) {
    AttributeExtendDto attributeExtendDto = attributeExtendService.add(
        attributeExtendParam.getCode(), attributeExtendParam.getDescription(),
        attributeExtendParam.getCategory(), attributeExtendParam.getSubcategory());
    return Response.success(attributeExtendDto);
  }

  @ApiOperation(value = "根据主键id更新扩展属性", notes = "不能存在两个code相同的扩展信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "扩展属性id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "code", dataType = "string", paramType = "query",
          defaultValue = "不传则不更新该字段"),
      @ApiImplicitParam(name = "category", value = "类型", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "subcategory", value = "子类型", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", dataType = "string",
          paramType = "query")})
  @Override
  public Response<Integer> updateAttributeExtend(AttributeExtendParam attributeExtendParam) {
    int count = attributeExtendService.updateByKey(attributeExtendParam.getId(),
        attributeExtendParam.getCode(), attributeExtendParam.getCategory(),
        attributeExtendParam.getSubcategory(), attributeExtendParam.getDescription());
    return Response.success(count);
  }
}

