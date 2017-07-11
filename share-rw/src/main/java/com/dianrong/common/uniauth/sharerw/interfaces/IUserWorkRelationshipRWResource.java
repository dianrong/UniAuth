package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserDetailInfoDto;
import com.dianrong.common.uniauth.common.bean.request.UserDetailInfoParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserWorkRelationshipResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * UserWorkRelationship的写接口定义.
 */
public interface IUserWorkRelationshipRWResource extends IUserWorkRelationshipResource {

  @POST
  @Path("add-or-update")
  Response<UserDetailInfoDto> addOrUpdateUserDetail(UserDetailInfoParam param);
  
  @POST
  @Path("update")
  Response<UserDetailInfoDto> updateUserDetail(UserDetailInfoParam param);
}
