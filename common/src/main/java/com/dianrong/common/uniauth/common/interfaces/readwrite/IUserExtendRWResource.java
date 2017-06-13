package com.dianrong.common.uniauth.common.interfaces.readwrite;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendParam;
import com.dianrong.common.uniauth.common.interfaces.read.IUserExtendResource;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 已被IAttributeExtendRWResource替代.
 *
 * @author wanglin
 * @see com.dianrong.common.uniauth.common.interfaces.readwrite.IAttributeExtendRWResource
 */
@Deprecated
public interface IUserExtendRWResource extends IUserExtendResource {

  @POST
  @Path("adduserextend")
  Response<UserExtendDto> addUserExtend(UserExtendParam userExtendParam);

  @POST
  @Path("updateuserextend")
  Response<Integer> updateUserExtend(UserExtendParam userExtendParam);
}

