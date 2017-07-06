package com.dianrong.common.uniauth.common.interfaces.read;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Profile的结构定义相关接口.
 */
@Path("profiles")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface IProfileResource {
  // scenario: get a profile
  /**
   * 获取一个Profile的定义.
   */
  @GET
  @Path("{profileId}")
  Response<ProfileDefinitionDto> getProfileDefinition(@PathParam("profileId") Long id);
}
