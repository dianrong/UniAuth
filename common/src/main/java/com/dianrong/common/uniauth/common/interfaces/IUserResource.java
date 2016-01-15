package com.dianrong.common.uniauth.common.interfaces;


import com.dianrong.common.uniauth.common.bean.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Created by Arc on 14/1/16.
 */
@Path("user")
@Produces({MediaType.APPLICATION_JSON})
public interface IUserResource {
    @GET
    @Path("test")
    Response<String> testResult();

}
