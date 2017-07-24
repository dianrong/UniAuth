package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.request.UserDetailInfoParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserDetailResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * UserDetail的写接口定义.
 */
public interface IUserDetailRWResource extends IUserDetailResource {

  @POST
  @Path("add-or-update")
  Response<UserDetailInfoDto> addOrUpdateUserDetail(UserDetailInfoParam param);
  
  @POST
  @Path("update")
  Response<UserDetailInfoDto> updateUserDetail(UserDetailInfoParam param);
}
