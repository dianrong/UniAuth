package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ConfigDto;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.request.CfgParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by Arc on 25/3/2016.
 */
@Path("config")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IConfigResource {
        /**
         * 获取文件list
         */
        @POST
        @Path("/query")
        Response<PageDto<ConfigDto>> queryConfig(CfgParam cfgParam);

        @GET
        @Path("/cfg-types")
        Response<Map<Integer, String>> getAllCfgTypes();

}
