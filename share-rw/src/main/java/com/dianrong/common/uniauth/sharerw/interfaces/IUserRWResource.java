package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDto;
import com.dianrong.common.uniauth.common.bean.request.UserParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Arc on 14/1/16.
 */
public interface IUserRWResource extends IUserResource {

  @POST
  @Path("addnewuser")
  // scenario: add new user
  Response<UserDto> addNewUser(UserParam userParam);

  @POST
  @Path("updateuser")
  // scenario: update user(including lock, disable, reset password, update profile)
  Response<UserDto> updateUser(UserParam userParam);

  @POST
  @Path("saverolestouser")
  // scenario: save roles to user
  Response<Void> saveRolesToUser(UserParam userParam);

  @POST
  @Path("resetpassword")
  // scenario: save roles to user
  Response<Void> resetPassword(UserParam userParam);

  @POST
  @Path("replacerolestouser")
  Response<Void> replaceRolesToUser(UserParam userParam);


  @POST
  @Path("savetagstouser")
  // scenario: techops user-tag
  Response<Void> replaceTagsToUser(UserParam userParam);
}
