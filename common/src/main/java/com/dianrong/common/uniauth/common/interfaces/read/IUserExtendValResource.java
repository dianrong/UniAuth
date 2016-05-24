package com.dianrong.common.uniauth.common.interfaces.read;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;

/**
 * @author wenlongchen
 * @since May 16, 2016
 */
@Path("userextendval")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserExtendValResource {

    @POST
    @Path("searchbyuseridandstatus")
    //scenario: search role
    Response<List<UserExtendValDto>> searchByUserId(UserExtendValParam userExtendValParam);

    @POST
    @Path("searchbyuseridandcode")
    Response<PageDto<UserExtendValDto>> searchByUserIdAndCode(UserExtendValParam userExtendValParam);
}

