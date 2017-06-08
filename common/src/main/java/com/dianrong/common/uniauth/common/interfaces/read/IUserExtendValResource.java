package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.PageDto;
import com.dianrong.common.uniauth.common.bean.dto.UserExtendValDto;
import com.dianrong.common.uniauth.common.bean.request.UserExtendValParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author wenlongchen.
 * @since May 16, 2016
 */
@Path("userextendval")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IUserExtendValResource {

  @POST
  @Path("searchbyuseridandstatus")
  // scenario: search role
  Response<List<UserExtendValDto>> searchByUserId(UserExtendValParam userExtendValParam);

  @POST
  @Path("searchbyuseridandcode")
  Response<PageDto<UserExtendValDto>> searchByUserIdAndCode(UserExtendValParam userExtendValParam);

  /**
   * search user extend val by user identity,identity include:phone number,email.
   *
   * @param userExtendValParam only concerned about status and identity
   * @see #searchByUserId(UserExtendValParam)
   */
  @POST
  @Path("searchbyuseridentity")
  Response<List<UserExtendValDto>> searchByUserIdentity(UserExtendValParam userExtendValParam);

  /**
   * 根据查询条件查询用户扩展属性值.
   *
   * @param userExtendValParam 查询条件对象
   * @return 符合条件的用户扩展属性值列表
   */
  @POST
  @Path("search")
  Response<List<UserExtendValDto>> search(UserExtendValParam userExtendValParam);
}

