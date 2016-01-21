package com.dianrong.common.uniauth.common.interfaces.rw;


import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.PrimaryKeyParam;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserResource;

/**
 * Created by Arc on 14/1/16.
 */
public interface IUserRWResource extends IUserResource {

    @POST
    @Path("addnewuser")
    //scenario: add new user
    Response<UserDto> addNewUser(UserParam userParam);

    @POST
    @Path("updateuser")
    //scenario: update user(including lock, disable, reset password, update profile)
    Response<String> updateUser(UserParam userParam);

    @POST
    @Path("deleteuser")
    //scenario: delete user, just for techops super admin
    Response<String> deleteUser(PrimaryKeyParam primaryKeyParam);
    
    @POST
    @Path("saverolestouser")
    //scenario: save roles to user
    Response<String> saveRolesToUser(UserParam userParam);
}
