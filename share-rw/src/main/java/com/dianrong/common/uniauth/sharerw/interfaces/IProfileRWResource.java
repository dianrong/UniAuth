package com.dianrong.common.uniauth.sharerw.interfaces;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.ProfileDefinitionDto;
import com.dianrong.common.uniauth.common.bean.request.ProfileDefinitionParam;
import com.dianrong.common.uniauth.common.interfaces.read.IProfileResource;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.cxf.jaxrs.ext.PATCH;

/**
 * Profile的结构定义相关接口.
 */
public interface IProfileRWResource extends IProfileResource{

  // scenario: define a new profile
  @POST
  Response<ProfileDefinitionDto> addNewProfileDefinition(ProfileDefinitionParam param);

  // scenario: update profile
  @POST
  @Path("{profileId}")
  Response<ProfileDefinitionDto> updateProfileDefinition(@PathParam("id") Long id,
      ProfileDefinitionParam param);

  // scenario: extend profile
  /**
   * 扩展属性和子Profile.
   */
  @PATCH
  @Path("{profileId}")
  Response<ProfileDefinitionDto> extendProfileDefinition(@PathParam("id") Long id,
      ProfileDefinitionParam param);
}
