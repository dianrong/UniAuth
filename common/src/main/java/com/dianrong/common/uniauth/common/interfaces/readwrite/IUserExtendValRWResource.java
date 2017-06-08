package com.dianrong.common.uniauth.common.interfaces.readwrite;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserExtendValResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * @author wenlongchen.
 * @since May 16, 2016
 */
public interface IUserExtendValRWResource extends IUserExtendValResource {

  @POST
  @Path("add")
  Response<UserExtendValDto> add(UserExtendValParam userExtendValParam);

  @POST
  @Path("delbyid")
  Response<Integer> delById(UserExtendValParam userExtendValParam);

  @POST
  @Path("updatebyid")
  Response<Integer> updateById(UserExtendValParam userExtendValParam);
}

