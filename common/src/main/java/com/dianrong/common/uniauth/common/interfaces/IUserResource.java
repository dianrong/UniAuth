package com.dianrong.common.uniauth.common.interfaces;


import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.Role;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Arc on 14/1/16.
 */
@Path("user")
@Produces({MediaType.APPLICATION_JSON})
public interface IUserResource {
    @POST
    @Path("rolecode")
    Response<java.util.List<Role>> getAllRoles();
}
