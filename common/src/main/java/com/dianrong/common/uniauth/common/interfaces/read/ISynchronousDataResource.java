package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.HrSynchronousLogDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.HrSynchronousLogParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 同步处理相关读操作接口.
 */
@Path("synchronous-data") @Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON}) public interface ISynchronousDataResource {

  @POST @Path("search-log") Response<PageDto<HrSynchronousLogDto>> querySynchronousLog(
      HrSynchronousLogParam param);
}

