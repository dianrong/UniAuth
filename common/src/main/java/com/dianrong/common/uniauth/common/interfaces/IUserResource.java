package com.dianrong.common.uniauth.common.interfaces;


import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by Arc on 14/1/16.
 */
@Path("user")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserResource {
    @POST
    @Path("test")
    Response<String> testResult(UserQuery userQuery);

}
