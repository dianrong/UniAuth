package com.dianrong.common.uniauth.common.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.RoleCodeDto;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailDto;
import com.dianrong.common.uniauth.common.bean.request.LoginParam;

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

    @POST
    @Path("login")
    //scenario: login and get user details info
    Response<UserDetailDto> getUserDetailInfo(LoginParam loginParam);
}
