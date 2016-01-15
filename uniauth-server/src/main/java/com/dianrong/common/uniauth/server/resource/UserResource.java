package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.request.UserQuery;
import com.dianrong.common.uniauth.common.interfaces.IUserResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Arc on 14/1/16.
 */
@RestController
public class UserResource implements IUserResource {
    @Override
    public Response<String> testResult(UserQuery userQuery) {
        throw new RuntimeException();
        //return Response.success();
    }
}
