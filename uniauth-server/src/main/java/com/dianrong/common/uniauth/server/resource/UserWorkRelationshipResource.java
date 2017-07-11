package com.dianrong.common.uniauth.server.resource;

import com.dianrong.common.uniauth.common.bean.Response;
import com.dianrong.common.uniauth.common.bean.dto.UserWorkRelationshipDto;
import com.dianrong.common.uniauth.common.bean.request.UserWorkRelationshipParam;
import com.dianrong.common.uniauth.server.service.UserWorkRelationshipService;
import com.dianrong.common.uniauth.sharerw.interfaces.IUserWorkRelationshipRWResource;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * 汇报线接口实现.
 */

@Api("汇报线关系处理")
@RestController
public class UserWorkRelationshipResource implements IUserWorkRelationshipRWResource {

  @Autowired
  private UserWorkRelationshipService userWorkRelationshipService;
  
  @Override
  public Response<UserWorkRelationshipDto> searchByUserId(UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.searchByUserId(param.getUserId()));
  }

  @Override
  public Response<UserWorkRelationshipDto> addOrUpdateUserWrokRelationship(UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.addOrUpdateUserWrokRelationship(param));
  }

  @Override
  public Response<UserWorkRelationshipDto> updateUserWrokRelationship(UserWorkRelationshipParam param) {
    return Response.success(userWorkRelationshipService.updateUserWrokRelationship(param));
  }
}
