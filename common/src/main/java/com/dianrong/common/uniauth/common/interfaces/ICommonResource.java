package com.dianrong.common.uniauth.common.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Arc on 15/1/16.
 */
@Path("common")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ICommonResource {

    @GET
    @Path("role/codes")
    Response<List<RoleCodeDto>> getAllRoleCodes();

}
