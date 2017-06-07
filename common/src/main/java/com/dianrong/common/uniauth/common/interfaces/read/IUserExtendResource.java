package com.dianrong.common.uniauth.common.interfaces.read;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendPageParam;

/**
 * 用户扩展属性操作读接口. 已被IAttributeExtendResource替代.
 * @see com.dianrong.common.uniauth.common.interfaces.read.IAttributeExtendResource
 * @author wanglin
 *
 */
@Deprecated
@Path("userextend")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserExtendResource {

  @POST
  @Path("searchuserextend")
    // scenario: search role
  Response<PageDto<UserExtendDto>> searchUserExtend(UserExtendPageParam pageParam);
}

