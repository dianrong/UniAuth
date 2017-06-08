package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Arc on 25/3/2016.
 */
@Path("config")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IConfigResource {

  /**
   * 获取文件list.
   */
  @POST
  @Path("/query")
  Response<PageDto<ConfigDto>> queryConfig(CfgParam cfgParam);

  @GET
  @Path("/cfg-types")
  Response<Map<Integer, String>> getAllCfgTypes();

}
