package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.HrSynchronousLogDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.HrSynchronousLogParam;
import com.dianrong.common.uniauth.server.synchronous.hr.service.Synchronous;
import com.dianrong.common.uniauth.sharerw.interfaces.ISynchronousDateRWResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 14/1/16.
 */
@Api("同步数据操作接口") @RestController public class SynchronousDataResource
    implements ISynchronousDateRWResource {

  @Autowired private Synchronous synchronous;

  @ApiOperation("根据条件查询同步日志,以日志发生时间的逆序返回") @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "startTime", value = "开始时间", dataType = "date", paramType = "query"),
      @ApiImplicitParam(name = "endTime", value = "结束时间", dataType = "date", paramType = "query"),
      @ApiImplicitParam(name = "type", value = "类型", dataType = "string", paramType = "query", allowableValues = "SYNCHRONOUS_HR_DATA, DELETE_FTP_HR_EXPIRED_DATA"),
      @ApiImplicitParam(name = "computerIp", value = "执行同步的服务器ip(模糊匹配)", dataType = "string", paramType = "query"),
      @ApiImplicitParam(name = "result", value = "结果", dataType = "string", paramType = "query", allowableValues = "SUCCESS, FAILURE"),
      @ApiImplicitParam(name = "pageSize", value = "每页大小", required = true, dataType = "integer", paramType = "query"),
      @ApiImplicitParam(name = "pageNumber", value = "页码(从0开始)", required = true, dataType = "integer", paramType = "query")})
  @Override
  public Response<PageDto<HrSynchronousLogDto>> querySynchronousLog(HrSynchronousLogParam param) {
    return Response.success(synchronous
        .queryHrSynchronousLog(param.getStartTime(), param.getEndTime(), param.getType(),
            param.getComputerIp(), param.getResult(), param.getPageNumber(), param.getPageSize()));
  }

  @ApiOperation("触发一次同步HR系统数据的操作") @Override public Response<Void> synchronousHrData() {
    synchronous.startSynchronize();
    return Response.success();
  }
}
