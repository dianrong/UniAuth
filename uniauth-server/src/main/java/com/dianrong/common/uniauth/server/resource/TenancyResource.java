package com.dianrong.common.uniauth.server.resource;

import com.codahale.metrics.annotation.Timed;
import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.TenancyDto;
import com.dianrong.common.uniauth.common.bean.request.TenancyParam;
import com.dianrong.common.uniauth.common.cons.AppConstants;
import com.dianrong.common.uniauth.server.service.TenancyService;
import com.dianrong.common.uniauth.sharerw.interfaces.ITenancyRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@Api("租户信息操作接口")
@RestController
public class TenancyResource implements ITenancyRWResource {

  @Autowired
  private TenancyService tenancyService;

  @ApiOperation("根据查询条件查询租户信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "租户Id", dataType = "long", paramType = "query"),
      @ApiImplicitParam(name = "code", value = "租户编码(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "status", value = "状态", dataType = "string", paramType = "query",
          allowableValues = "0,1"),
      @ApiImplicitParam(name = "name", value = "租户的名称(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "contactName", value = "根据租户的联系人名称匹配(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "phone", value = "根据租户的联系电话匹配(模糊匹配)", dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "根据租户的描述匹配(模糊匹配)", dataType = "string",
          paramType = "query")})
  @Override
  @Timed
  public Response<List<TenancyDto>> searchTenancy(TenancyParam tenancyParam) {
    List<TenancyDto> tenancyDtoList = tenancyService.getAllTenancy(tenancyParam.getId(),
        tenancyParam.getCode(), tenancyParam.getStatus(), tenancyParam.getName(),
        tenancyParam.getContactName(), tenancyParam.getPhone(), tenancyParam.getDescription());
    return new Response<List<TenancyDto>>(tenancyDtoList);
  }

  @ApiOperation("获取默认的租户的信息")
  @Override
  public Response<TenancyDto> queryDefaultTenancy() {
    return new Response<TenancyDto>(
        tenancyService.getEnableTenancyByCode(AppConstants.DEFAULT_TANANCY_CODE));
  }

  @ApiOperation("根据租户编码查询可用的租户信息")
  @ApiImplicitParams(value = {@ApiImplicitParam(name = "code", value = "租户编码", required = true,
      dataType = "string", paramType = "query")})
  @Override
  public Response<TenancyDto> queryEnableTenancyByCode(TenancyParam param) {
    String tenancyCode = param.getCode();
    if (!StringUtils.hasLength(tenancyCode)) {
      return Response.success(null);
    }
    return new Response<TenancyDto>(tenancyService.getEnableTenancyByCode(tenancyCode));
  }

  @ApiOperation("新增租户")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "code", value = "租户编码", required = true, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "name", value = "租户名称", required = false, dataType = "string",
          paramType = "query", defaultValue = "使用code"),
      @ApiImplicitParam(name = "contactName", value = "联系人", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "phone", value = "联系电话", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "adminEmail", value = "租户管理员邮箱", required = true,
          dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "adminPassword", value = "租户编码", required = true,
          dataType = "string", paramType = "query")})
  @Override
  public Response<TenancyDto> addTenancy(TenancyParam tenancyParam) {
    return Response
        .success(tenancyService.addNewTenancy(tenancyParam.getCode(), tenancyParam.getName(),
            tenancyParam.getContactName(), tenancyParam.getPhone(), tenancyParam.getDescription(),
            tenancyParam.getAdminEmail(), tenancyParam.getAdminPassword()));
  }

  @ApiOperation("更新租户信息")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "id", value = "主键id", required = true, dataType = "long",
          paramType = "query"),
      @ApiImplicitParam(name = "code", value = "租户编码", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "name", value = "租户名称", required = false, dataType = "string",
          paramType = "query", defaultValue = "使用code"),
      @ApiImplicitParam(name = "contactName", value = "联系人", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "phone", value = "联系电话", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "description", value = "描述", required = false, dataType = "string",
          paramType = "query"),
      @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int",
          paramType = "query")})
  @Override
  public Response<TenancyDto> updateTenancy(TenancyParam tenancyParam) {
    return Response.success(tenancyService.updateTenancy(tenancyParam.getId(),
        tenancyParam.getCode(), tenancyParam.getName(), tenancyParam.getContactName(),
        tenancyParam.getPhone(), tenancyParam.getDescription(), tenancyParam.getStatus()));
  }
}
