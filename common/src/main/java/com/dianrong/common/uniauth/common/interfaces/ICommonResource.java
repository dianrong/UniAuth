package com.dianrong.common.uniauth.common.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.GroupCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Arc on 15/1/16.
 */
@Path("common")
@Produces({MediaType.APPLICATION_JSON})
public interface ICommonResource {

    @GET
    @Path("role/codes")
    Response<List<RoleCodeDto>> getAllRoleCodes();

    @GET
    @Path("group/codes")
    Response<List<GroupCodeDto>> getAllGroupCodes();
}
